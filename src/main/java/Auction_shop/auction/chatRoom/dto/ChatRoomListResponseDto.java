package Auction_shop.auction.chatRoom.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatRoomListResponseDto {

    private Long userId;
    private Long yourId;
    private Long postId;
    private Long roomId;

    private String imageUrl;                // 물품 이미지 url
    private String ProfileUrl;               // 상대방 프로필 url
    private String nickname;                // 상대방 닉네임
    private LocalDateTime latestChatTime;   // 마지막 채팅 시간
    private String latestChatLog;           // 마지막 채팅 내역
}
