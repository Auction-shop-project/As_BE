package Auction_shop.auction.domain.member.controller;

import Auction_shop.auction.domain.member.Member;
import Auction_shop.auction.domain.member.service.MemberService;
import Auction_shop.auction.web.dto.MemberResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponseDto> getByMemberId(@PathVariable Long memberId){
        Member member = memberService.getById(memberId);
        MemberResponseDto collect = MemberResponseDto.builder()
                .id(member.getId())
                .username(member.getUsername())
                .name(member.getName())
                .address(member.getAddress())
                .phone(member.getPhone())
                .point(member.getPoint())
                .build();

        return ResponseEntity.ok(collect);
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long memberId){
        memberService.deleteMember(memberId);
        return ResponseEntity.noContent().build();
    }
}
