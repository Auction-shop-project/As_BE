package Auction_shop.auction.chat.controller;

import Auction_shop.auction.chat.domain.Chat;
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

    @MessageMapping("/chat/{roomId}")
    public void processMessage(@DestinationVariable String roomId,Chat chat) throws Exception {
        String destination = "/sub/roomId/" + roomId;
        chatService.createChat(chat);
        try {
            messagingTemplate.convertAndSend(destination, chat.get메세지());
        } catch (Exception e) {
            log.error("ChatError = {}", e);
            messagingTemplate.convertAndSend(destination, "Error:" + e.getMessage());
            /// TODO: API 명세서에 에러처리 내용 작성
        }
        /// TODO: 26일 회의 질문 사항 - 클라이언트에서 채팅내역 어떻게 관리하는지? 관리 어렵다면 채팅방 열 때 서버에서 이전 채팅 기록들 가져오도록 구현 추가
    }

}
