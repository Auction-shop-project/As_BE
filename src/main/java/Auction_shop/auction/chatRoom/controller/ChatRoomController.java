package Auction_shop.auction.chatRoom.controller;

import Auction_shop.auction.chatRoom.domain.ChatRoom;
import Auction_shop.auction.chatRoom.dto.ChatRoomCreateResponseDto;
import Auction_shop.auction.chatRoom.dto.ChatRoomInfoResponseDto;
import Auction_shop.auction.chatRoom.dto.ChatRoomListResponseDto;
import Auction_shop.auction.chatRoom.service.ChatRoomService;
import Auction_shop.auction.sse.SSEConnection;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@Slf4j
public class ChatRoomController {
    @Qualifier("longRedisTemplate")
    private final RedisTemplate<String, Long> messageQueue;
    private final SSEConnection sseConnection;
    private final ChatRoomService chatRoomService;
    private final Logger LOGGER = LoggerFactory.getLogger(ChatRoomController.class);

    /**
     *  채팅방 리스트 불러오기
     */
    @GetMapping("/chatroom/list/{userId}")
    public ResponseEntity<List<ChatRoomListResponseDto>> findChatRooms(@PathVariable Long userId) {
        List<ChatRoomListResponseDto> chatRooms = chatRoomService.findChatRoomsByUserId(userId);
        LOGGER.debug("chatRooms's size = {}", chatRooms.size());
        return ResponseEntity.ok(chatRooms);
    }

    /**
     *  채팅방 입장
     *  case 1 : 채팅방이 없는 상태라면 채팅방 생성 후 채팅방 번호 응답 (채팅을 처음 시작하는 경우)
     *  case 2 : 채팅방이 있는 상태라면 채팅방 정보와 채팅 내역을 불러와 응답
     */
    @GetMapping("/chatroom/enter/{userId}/{yourId}/{postId}")
    public ResponseEntity<?> enter(@PathVariable Long userId, @PathVariable Long yourId, @PathVariable Long postId) {
        LOGGER.debug("userId={},yourId={},postId={}", userId, yourId, postId);
        Optional<ChatRoom> chatRoomInfo = chatRoomService.findChatRoomInfo(userId, yourId, postId);

        // case 1
        if (chatRoomInfo.isEmpty()) {
            LOGGER.debug("enter case 1 : chatRoomInfo is Empty");
            Long roomId = chatRoomService.createNewChatRoom(userId, yourId, postId);
            SseEmitter emitter = sseConnection.getEmitter(yourId);  // 상대방ID를 통해 상대방의 emitter를 가져옴
            if (emitter != null) {
                LOGGER.debug("ChatRoomController: emitter != null");
                sseConnection.sendEvent(emitter, "createdNewChatRoom", roomId);  // SSE를 통해 상대방에게 알림
            }
            if (emitter == null) {  // 상대방이 접속 중이 아닌 상태일 때 메세지 큐에 저장 후 나중에 상대방이 접속 시 생성된 채팅방 번호 제공
                LOGGER.debug("ChatRoomController: emitter == null");
                // Redis의 key로 문자열이 들어가기에 Long타입의 yourId를 String으로 변환하여 저장
                String idTypeChange = String.valueOf(yourId);
                messageQueue.opsForList().leftPush(idTypeChange, roomId);  // 메세지 큐에 저장
            }
            ChatRoomCreateResponseDto chatRoomCreateResponseDto = new ChatRoomCreateResponseDto();
            chatRoomCreateResponseDto.setRoomId(roomId);
            return ResponseEntity.ok(chatRoomCreateResponseDto);
        }

        // case 2
        LOGGER.debug("enter case 2 : chatRoomInfo is not Empty");
        ChatRoom chatRoom = chatRoomInfo.get();

        // roomId를 꺼내 chat 테이블 조회
        ChatRoomInfoResponseDto chatInfo = chatRoomService.enterChatRoom(chatRoom.getRoomId());
        return ResponseEntity.ok(chatInfo);
    }

    /**
     * 채팅방 퇴장
     */
    @PostMapping("/chatroom/delete/{userId}/{postId}")
    public ResponseEntity<String> delete(@PathVariable Long userId, @PathVariable Long postId) {
        ChatRoom deleteRoom = chatRoomService.deleteChatRoom(userId, postId);

        if (deleteRoom == null) {
            return ResponseEntity.status(404).body("delete failed");
        }

        return ResponseEntity.ok("delete success");
    }
}
