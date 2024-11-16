package Auction_shop.auction.chat.controller;

import Auction_shop.auction.chat.domain.Chat;
import Auction_shop.auction.chat.dto.ChatDto;
import Auction_shop.auction.chat.service.ChatService;
import Auction_shop.auction.chatRoom.domain.ChatRoom;
import Auction_shop.auction.chatRoom.service.ChatRoomService;
import Auction_shop.auction.domain.member.Member;
import Auction_shop.auction.sse.SSEConnection;
import Auction_shop.auction.web.fcm.service.FirebaseCloudMessageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Controller
@AllArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;
    private final FirebaseCloudMessageService fcmService;
    private final ChatRoomService chatRoomService;
    private final SSEConnection sseConnection;
    private final Logger LOGGER = LoggerFactory.getLogger(ChatController.class);
    private final RedisTemplate<String, Long> messageQueue;

    @MessageMapping("/chatroom/{roomId}")
    public void processMessage(@DestinationVariable String roomId, Chat chat) {
        String destination = "/sub/chatroom/" + roomId;
        Chat savedChat = chatService.createChat(chat);

        ChatDto response = new ChatDto(
                savedChat.getId(),
                savedChat.getRoomId(),
                savedChat.getUserId(),
                savedChat.getMessage(),
                savedChat.getCreatedAt()
        );

        try {
//            ChatRoom findOpponent = chatRoomService.isOtherUserLeft(response.getUserId(), roomId);
//
//            //상대방이 채팅방을 나간 경우
//            if (findOpponent == null) {
//                SseEmitter emitter = sseConnection.getEmitter(yourId);  // 상대방ID를 통해 상대방의 emitter를 가져옴
//            }
//            if (emitter != null) {
//                LOGGER.debug("ChatController: emitter != null");
//                sseConnection.sendEvent(emitter, "re-entry", roomId);  // SSE를 통해 상대방에게 알림
//            }
//            if (emitter == null) {  // 상대방이 접속 중이 아닌 상태일 때 메세지 큐에 저장 후 나중에 상대방이 접속 시 생성된 채팅방 번호 제공
//                LOGGER.debug("ChatController: emitter == null");
//                // Redis의 key로 문자열이 들어가기에 Long타입의 yourId를 String으로 변환하여 저장
//                String idTypeChange = String.valueOf(yourId);
//                messageQueue.opsForList().leftPush(idTypeChange, roomId);  // 메세지 큐에 저장
//            }

            messagingTemplate.convertAndSend(destination, response);   // 도착 경로로 메세지 전달
            Member member = chatRoomService.findMemberByChatRoom(Long.parseLong(roomId));

            //채팅 받는 상대에게 푸시알림
            fcmService.sendMessageTo(member.getDeviceToken(),
                    "채팅 도착!",
                    chat.getMessage(),
                    roomId,
                    "CHAT");
        } catch (Exception e) {
            log.error("ChatError = {}", e);
            messagingTemplate.convertAndSend(destination, "Error:" + e.getMessage());
        }
    }
}
