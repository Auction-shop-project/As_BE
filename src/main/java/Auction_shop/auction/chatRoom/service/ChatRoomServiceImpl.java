package Auction_shop.auction.chatRoom.service;

import Auction_shop.auction.chat.dto.ChatDto;
import Auction_shop.auction.chat.dto.ChatLogDto;
import Auction_shop.auction.chat.repository.ChatRepository;
import Auction_shop.auction.chatRoom.domain.ChatRoom;
import Auction_shop.auction.chatRoom.dto.ChatRoomInfoResponseDto;
import Auction_shop.auction.chatRoom.dto.ChatRoomListResponseDto;
import Auction_shop.auction.chatRoom.repository.ChatRoomRepository;
import Auction_shop.auction.domain.member.Member;
import Auction_shop.auction.domain.member.service.MemberService;
import Auction_shop.auction.domain.product.Product;
import Auction_shop.auction.domain.product.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ChatRoomServiceImpl implements ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final ProductService productService;
    private final MemberService memberService;

    /**
     * 채팅방 목록 가져오기
     * @return list(채팅방 목록)
     */
    @Override
    public List<ChatRoomListResponseDto> findChatRoomsByUserId(Long userId) {
        List<ChatRoom> list = chatRoomRepository.findByUserId(userId);
        log.info("채팅리스트={}", list.size());
        List<ChatRoomListResponseDto> ResponseList = new ArrayList<>();

        for (ChatRoom chatRoom : list) {
            Long postId = chatRoom.getPostId();
            log.info("게시물ID={}", postId);
            Product product = productService.findProductById(postId);
            // 이미지 없을수도 있음
            String firstImageUrl = product.getImageUrls().stream().findFirst().orElse(null);

            Long yourId = chatRoom.getYourId();
            log.info("상대방ID={}", yourId);
            Member member = memberService.getById(yourId);
            // 이미지 없을수도 있음
            String firstProfileUrl;
            if (member.getProfileImage() != null) {
                firstProfileUrl = member.getProfileImage().getAccessUrl();
            }else{
                firstProfileUrl = null;
            }
            String nickname = member.getNickname();

            Long roomId = chatRoom.getRoomId();
            log.info("방번호ID={}", roomId);
            ChatDto latestChatInfo = chatRepository.findLatestChatByRoomId(roomId);
            log.info("최근채팅방정보 조회 완료");
            String latestChatLog;
            LocalDateTime latestChatTime;
            if(latestChatInfo != null) {
                latestChatLog = latestChatInfo.getMessage();
                latestChatTime = latestChatInfo.getCreatedAt();
            }else{
                latestChatLog = null;
                latestChatTime = null;
            }

            ChatRoomListResponseDto responseDto = new ChatRoomListResponseDto();
            responseDto.setUserId(userId);
            responseDto.setYourId(yourId);
            responseDto.setPostId(postId);
            responseDto.setRoomId(roomId);

            responseDto.setImageUrl(firstImageUrl);
            responseDto.setProfileUrl(firstProfileUrl);
            responseDto.setNickname(nickname);
            responseDto.setLatestChatTime(latestChatTime);
            responseDto.setLatestChatLog(latestChatLog);

            ResponseList.add(responseDto);
        }

        return ResponseList;
    }

    /**
     * 채팅방 존재유무 확인
     * @return Optional.empty() or 조회된 객체
     */
    @Override
    public Optional<ChatRoom> findChatRoomInfo(Long userId, Long yourId, Long postId) {
        return Optional.ofNullable(chatRoomRepository.findByUserIdAndYourIdAndPostId(userId, yourId, postId));
    }

    /**
     * 채팅방 생성
     * @return roomId(생성한 채팅방 번호)
     */
    @Override
    public Long createNewChatRoom(Long userId, Long yourId, Long postId) {
        Long roomId = chatRoomRepository.findChatRoomSize() + 1;
        ChatRoom myChatRoomList = ChatRoom.builder().userId(userId).yourId(yourId).postId(postId).roomId(roomId).build();
        ChatRoom yourChatRoomList = ChatRoom.builder().userId(yourId).yourId(userId).postId(postId).roomId(roomId).build();
        chatRoomRepository.save(myChatRoomList);
        chatRoomRepository.save(yourChatRoomList);
        return roomId;
    }

    /**
     * roomId를 가진 채팅방의 채팅 내역 전달
     * @return chatLog(상품명, 채팅 내역, 현재가격)
     */
    @Override
    public ChatRoomInfoResponseDto enterChatRoom(Long roomId) {
        List<ChatLogDto> chatLog = chatRepository.findByRoomId(roomId);
        
        ChatRoom chatRoom = chatRoomRepository.findFirstByRoomId(roomId);
        Long postId = chatRoom.getPostId();
        int currentPriceById = productService.findCurrentPriceById(postId);

        Product product = productService.findProductById(postId);
        String title = product.getTitle();

        log.info("roomId={}", roomId);
        ChatRoomInfoResponseDto infoResponseDto = new ChatRoomInfoResponseDto();
        infoResponseDto.setChatLog(chatLog);
        infoResponseDto.setRoomId(roomId);
        infoResponseDto.setCurrentPrice(currentPriceById);
        infoResponseDto.setTitle(title);

        log.info("infoResponseDto.getRoomId={}", infoResponseDto.getRoomId());
        return infoResponseDto;
    }

    /**
     *
     * @param roomId
     * @return member
     */
    @Override
    public Member findMemberByChatRoom(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(()-> new RuntimeException("Room not found"));
        Member member = memberService.getById(chatRoom.getYourId());
        return member;
    }

    /**
     * 채팅방 퇴장한 유저 정보 삭제
     */
    @Transactional
    @Override
    public ChatRoom deleteChatRoom(Long userId, Long postId) {
        ChatRoom deleteRoom = chatRoomRepository.deleteChatRoomByUserIdAndPostId(userId, postId);
        return deleteRoom;
    }

    @Override
    public ChatRoom isOtherUserLeft(Long userId, String roomId) {
        ChatRoom findOpponent = chatRoomRepository.findByYourIdAndRoomId(userId, roomId);
        return findOpponent;
    }
}
