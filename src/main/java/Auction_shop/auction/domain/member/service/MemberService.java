package Auction_shop.auction.domain.member.service;

import Auction_shop.auction.domain.bid.BidStatus;
import Auction_shop.auction.domain.bid.service.BidService;
import Auction_shop.auction.domain.image.Image;
import Auction_shop.auction.domain.image.service.ImageService;
import Auction_shop.auction.domain.address.Address;
import Auction_shop.auction.domain.member.Member;
import Auction_shop.auction.domain.address.repository.AddressRepository;
import Auction_shop.auction.domain.member.repository.MemberRepository;
import Auction_shop.auction.domain.product.ProductDocument;
import Auction_shop.auction.domain.product.repository.ProductJpaRepository;
import Auction_shop.auction.domain.product.service.ProductService;
import Auction_shop.auction.domain.refreshToken.repository.RefreshTokenRepository;
import Auction_shop.auction.web.dto.bid.MemberBidListResponseDto;
import Auction_shop.auction.web.dto.member.MemberResponseDto;
import Auction_shop.auction.web.dto.member.MemberUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final AddressRepository addressRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ProductJpaRepository productRepository;
    private final BidService bidService;
    private final ProductService productService;
    private final ImageService imageService;

    @Value("${heybid.admin.key}")
    private String adminKey;

    @Transactional
    public Member save(String uuid, String deviceToken){
        Member member = Member.builder()
                .uuid(uuid)
                .role("USER")
                .name("TEST")
                .nickname(uuid)
                .email(uuid)
                .phone("TEST")
                .deviceToken(deviceToken)
                .point(0l)
                .build();
        memberRepository.save(member);
        return member;
    }

    public Member getByUuid(String uuid){
        Optional<Member> findMember = memberRepository.findByUuid(uuid);
        if(findMember.isEmpty()) return null;

        return findMember.get();
    }

    public Member getById(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException(memberId + "에 해당하는 회원이 없습니다."));
        return member;
    }

    @Transactional
    public Member updateMember(Long memberId, MemberUpdateDto memberUpdateDto, MultipartFile image){

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException(memberId + "에 해당하는 회원이 없습니다."));

        if (!member.getNickname().equals(memberUpdateDto.getNickname()) &&
                memberRepository.existsByNickname(memberUpdateDto.getNickname())) {
            throw new RuntimeException("방금 누군가가 해당 닉네임으로 닉네임 변경을 했습니다.");
        }

        if (!member.getNickname().equals(memberUpdateDto.getNickname())){
            productService.updateCreateBy(member.getNickname(), memberUpdateDto.getNickname(), memberId);
        }

        if (memberUpdateDto.isChangeImage()){
            if (member.getProfileImage() != null) {
                imageService.deleteImage(member.getProfileImage().getStoredName());
            }
            if (image == null && image.isEmpty()){
                member.setProfileImage(null);
            }else {
                Image profileImage = imageService.saveImage(image);
                member.setProfileImage(profileImage);
            }
        }

        if (memberUpdateDto.getEmail().equals(adminKey)){
            member.updateRole("ADMIN");
        }else{
            member.updateRole("USER");
        }

        Address address = null;
        if (memberUpdateDto.getAddress() != null) {
            address = Address.builder()
                    .address(memberUpdateDto.getAddress())
                    .detailAddress(memberUpdateDto.getDetailAddress())
                    .zipcode(memberUpdateDto.getZipcode())
                    .phoneNumber(memberUpdateDto.getPhone())
                    .name(memberUpdateDto.getName())
                    .defaultAddress(true)
                    .build();

            addressRepository.save(address);
        }

        member.update(memberUpdateDto.getName() ,memberUpdateDto.getNickname(), memberUpdateDto.getEmail(), memberUpdateDto.getPhone(), address, memberUpdateDto.getCategories());

        return member;
    }

    public List<Member> getTop3MembersByPoints() {
        return memberRepository.findTop3ByOrderByPointDesc();
    }

    public MemberResponseDto getMemberByRefreshToken(String refreshToken){

        String uuid = refreshTokenRepository.getUsernameByRefreshToken(refreshToken);
        if (uuid == null) {
            return null;
        }
        Optional<Member> findMember = memberRepository.findByUuid(uuid);
        if (findMember.isEmpty()) {
            return null;
        }
        Member member = findMember.get();

        MemberResponseDto memberResponseDto = MemberResponseDto.builder()
                .id(member.getId())
                .username(member.getUuid())
                .name(member.getName())
                .nickname(member.getNickname())
                .phone(member.getPhone())
                .point(member.getPoint())
                .role(member.getRole())
                .build();

        return memberResponseDto;
    }

    public boolean nameCheck(String nickname){
        return memberRepository.existsByNickname(nickname);
    }

    @Transactional
    public void deleteMember(Long memberId) {
        List<MemberBidListResponseDto> memberBid = bidService.getMemberBid(memberId);
        boolean isBiddingActive = memberBid.stream().anyMatch(b -> b.getBidStatus() == BidStatus.PROGRESS);
        if(isBiddingActive){
            throw new RuntimeException("해당 유저가 입찰을 진행 중 입니다.");
        }

        boolean isSellingActive =  productRepository.existsActiveProductsByMemberId(memberId);
        if(isSellingActive){
            throw new RuntimeException("해당 유저가 판매를 진행 중 입니다.");
        }

        memberRepository.deleteById(memberId);
    }

}
