package Auction_shop.auction.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@AllArgsConstructor
public class ChatDto {
    private String id;
    private Long roomId; // 채팅방번호
    private String userId;
    private String message;
    private LocalDateTime createdAt;
}
