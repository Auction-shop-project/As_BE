package Auction_shop.auction.chatRoom.service;

import Auction_shop.auction.chatRoom.domain.ChatRoom;
import Auction_shop.auction.chatRoom.dto.ChatRoomInfoResponseDto;
import Auction_shop.auction.chatRoom.dto.ChatRoomListResponseDto;
import Auction_shop.auction.domain.member.Member;

import java.util.List;
import java.util.Optional;

public interface ChatRoomService {
    List<ChatRoomListResponseDto> findChatRoomsByUserId(Long userId);
    Optional<ChatRoom> findChatRoomInfo(Long userId, Long yourId, Long postId);
    Long createNewChatRoom(Long userId, Long yourId, Long postId);
    ChatRoomInfoResponseDto enterChatRoom(Long roomId);
    Member findMemberByChatRoom(Long roomId);
    ChatRoom deleteChatRoom(Long userId, Long roomId);
    ChatRoom isOtherUserLeft(Long userId, Long roomId);
}
