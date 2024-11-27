package Auction_shop.auction.chatRoom.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;  // 나의 ID

    @Column(nullable = false)
    private Long yourId;  // 상대방ID

    @Column(nullable = false)
    private Long postId;  // 게시글ID
    private Long roomId;    // 채팅방번호

    @Builder
    public ChatRoom(Long userId, Long yourId, Long postId, Long roomId) {
        this.userId = userId;
        this.yourId = yourId;
        this.postId = postId;
        this.roomId = roomId;
    }
}
