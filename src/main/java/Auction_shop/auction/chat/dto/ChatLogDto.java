package Auction_shop.auction.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ChatLogDto {
    private String id;
    private Long userId;
    private String message;
    private LocalDateTime createdAt;
}
