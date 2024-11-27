package Auction_shop.auction.domain.member.controller;

import Auction_shop.auction.domain.member.Member;
import Auction_shop.auction.domain.member.service.MemberService;
import Auction_shop.auction.security.jwt.JwtUtil;
import Auction_shop.auction.web.dto.member.MemberListResponseDto;
import Auction_shop.auction.web.dto.member.MemberMapper;
import Auction_shop.auction.web.dto.member.MemberResponseDto;
import Auction_shop.auction.web.dto.member.MemberUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final MemberMapper memberMapper;
    private final JwtUtil jwtUtil;

    @GetMapping()
    public ResponseEntity<MemberResponseDto> getByMemberId(@RequestHeader("Authorization") String authorization){
        Long memberId = jwtUtil.extractMemberId(authorization);
        Member member = memberService.getById(memberId);
        MemberResponseDto collect = memberMapper.toResponseDto(member);
        return ResponseEntity.ok(collect);
    }

    @PatchMapping("/{memberId}")
    public ResponseEntity<MemberResponseDto> updateMember(@PathVariable("memberId") Long memberId,
                                                          @RequestPart(value = "member") MemberUpdateDto memberUpdateDto,
                                                          @RequestPart(value = "image", required = false) MultipartFile image){
        Member member = memberService.updateMember(memberId, memberUpdateDto, image);
        MemberResponseDto collect = memberMapper.toResponseDto(member);
        return ResponseEntity.ok(collect);
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteMember(@RequestHeader("Authorization") String authorization){
        System.out.println("진입");
        Long memberId = jwtUtil.extractMemberId(authorization);
        memberService.deleteMember(memberId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/name")
    public ResponseEntity<Boolean> nameCheck(@RequestParam String nickname){

        if (nickname == null || nickname.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(false);
        }

        boolean isDuplicate = memberService.nameCheck(nickname);
        return ResponseEntity.ok(!isDuplicate);
    }

    @GetMapping("/point")
    public ResponseEntity<List<MemberListResponseDto>> getPoint(){
        List<Member> members = memberService.getTop3MembersByPoints();
        List<MemberListResponseDto> collect = members.stream()
                .map(memberMapper::toListResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(collect);
    }
}
