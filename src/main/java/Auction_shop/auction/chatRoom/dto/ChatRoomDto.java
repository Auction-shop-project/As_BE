package Auction_shop.auction.chatRoom.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatRoomDto {

    private Long userId;  // 유저ID
    private Long postId;  // 게시물ID
    private Long yourId;  // 상대방ID

    @Builder
    public ChatRoomDto(Long userId, Long postId, Long yourId) {
        this.userId = userId;
        this.postId = postId;
        this.yourId = yourId;
    }
}
