package Auction_shop.auction.chatRoom.dto;

import Auction_shop.auction.chat.dto.ChatLogDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChatRoomInfoResponseDto {

    private List<ChatLogDto> chatLog;
    private Long roomId;
    private int currentPrice;
    private String title;
}
