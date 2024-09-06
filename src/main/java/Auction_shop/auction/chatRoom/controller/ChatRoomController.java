package Auction_shop.auction.chatRoom.controller;

import Auction_shop.auction.chat.dto.ChatDto;
import Auction_shop.auction.chatRoom.domain.ChatRoom;
import Auction_shop.auction.chatRoom.dto.ChatRoomDto;
import Auction_shop.auction.chatRoom.service.ChatRoomService;
import Auction_shop.auction.sse.SSEConnection;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@Slf4j
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    // 채팅방 리스트 불러오기
    @GetMapping("/chatroom/list/{userId}")
    public ResponseEntity<List<ChatRoom>> findChatRooms(@PathVariable String userId) {
        List<ChatRoom> chatRooms = chatRoomService.findChatRoomsByUserId(userId);
        return ResponseEntity.ok(chatRooms);
    }

    // 채팅방 입장
    // case 1 : 채팅방이 없는 상태라면 채팅방 생성 후 채팅방 번호 응답 (채팅을 처음 시작하는 경우)
    // case 2 : 채팅방이 있는 상태라면 채팅방 정보와 채팅 내역을 불러와 응답

    @GetMapping("/chatroom/enter")
    public ResponseEntity<?> enter(@RequestBody ChatRoomDto chatRoomDto) {
        String userId = chatRoomDto.getUserId();
        String yourId = chatRoomDto.getYourId();
        String postId = chatRoomDto.getPostId();
        Optional<ChatRoom> chatRoomInfo = chatRoomService.findChatRoomInfo(userId, yourId, postId);

        // case 1
        if (chatRoomInfo.isEmpty()) {
            Long newChatRoomId = chatRoomService.createNewChatRoom(userId, yourId, postId);
            SSEConnection sseConnection = new SSEConnection();
            SseEmitter emitter = sseConnection.getEmitter(yourId);  // 상대방ID를 통해 상대방의 emitter를 가져옴
            sseConnection.sendEvent(emitter, "createdNewChatRoom", newChatRoomId);  // SSE를 통해 상대방에게 알림

            return ResponseEntity.ok(newChatRoomId);
        }

        // case 2
        ChatRoom chatRoom = chatRoomInfo.get();

        // roomId를 꺼내 chat 테이블 조회
        List<ChatDto> chatLog = chatRoomService.fetchChatLog(chatRoom.getRoomId());
        return ResponseEntity.ok(chatLog);
    }
}
