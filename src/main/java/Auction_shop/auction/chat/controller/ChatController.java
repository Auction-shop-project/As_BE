package Auction_shop.auction.chat.controller;

import Auction_shop.auction.chat.domain.Chat;
import Auction_shop.auction.chat.dto.ChatDto;
import Auction_shop.auction.chat.service.ChatService;
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

    @Autowired
    public ChatController(SimpMessagingTemplate messagingTemplate, ChatService chatService) {
        this.messagingTemplate = messagingTemplate;
        this.chatService = chatService;
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
        } catch (Exception e) {
            log.error("ChatError = {}", e);
            messagingTemplate.convertAndSend(destination, "Error:" + e.getMessage());
        }
    }
}
