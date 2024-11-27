package Auction_shop.auction.chatRoom.repository;

import Auction_shop.auction.chatRoom.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findByUserId(Long userId);
    ChatRoom findFirstByRoomId(Long roomId);
    ChatRoom findByUserIdAndYourIdAndPostId(Long userId, Long yourId, Long postId);
    ChatRoom deleteChatRoomByUserIdAndRoomId(Long userId, Long roomId);
    ChatRoom findByYourIdAndRoomId(Long userId, Long roomId);

    @Query("SELECT COUNT(CR) FROM ChatRoom CR")
    Long findChatRoomSize();
}
