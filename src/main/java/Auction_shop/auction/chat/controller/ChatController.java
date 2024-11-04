package Auction_shop.auction.chat.controller;

import Auction_shop.auction.chat.domain.Chat;
import Auction_shop.auction.chat.dto.ChatDto;
import Auction_shop.auction.chat.service.ChatService;
import Auction_shop.auction.chatRoom.service.ChatRoomService;
import Auction_shop.auction.domain.member.Member;
import Auction_shop.auction.web.fcm.service.FirebaseCloudMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;
    private final FirebaseCloudMessageService fcmService;
    private final ChatRoomService chatRoomService;

    @Autowired
    public ChatController(SimpMessagingTemplate messagingTemplate, ChatService chatService, FirebaseCloudMessageService fcmService, ChatRoomService chatRoomService) {
        this.messagingTemplate = messagingTemplate;
        this.chatService = chatService;
        this.fcmService = fcmService;
        this.chatRoomService = chatRoomService;
    }

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
