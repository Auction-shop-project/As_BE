package Auction_shop.auction.domain.bid.controller;

import Auction_shop.auction.domain.alert.AlertType;
import Auction_shop.auction.domain.alert.util.AlertUtil;
import Auction_shop.auction.domain.bid.service.BidService;
import Auction_shop.auction.domain.member.Member;
import Auction_shop.auction.domain.payments.service.AscendingPaymentService;
import Auction_shop.auction.domain.product.repository.ProductJpaRepository;
import Auction_shop.auction.security.jwt.JwtUtil;
import Auction_shop.auction.web.dto.bid.BidResponseDto;
import Auction_shop.auction.web.dto.bid.MemberBidListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bids")
public class BidController {

    private final BidService bidService;
    private final AscendingPaymentService ascendingPaymentService;
    private final ProductJpaRepository productJpaRepository;
    private final AlertUtil alertUtil;
    private final JwtUtil jwtUtil;

    //상향식 입찰 넣기
    @PostMapping("/{productId}/{impUid}")
    public ResponseEntity<Map<String, Object>> addBid(@RequestHeader("Authorization") String authorization,
                                                 @PathVariable("productId") Long productId,
                                                 @PathVariable String impUid) {
        Long memberId = jwtUtil.extractMemberId(authorization);
        String paymentResult;
        try {
            // 결제 검증
            ResponseEntity<Map<String, Object>> collect = ascendingPaymentService.PaymentsVerify(impUid, productId, memberId);

            // 입찰 성공 알림 (옵션)
            Member member = productJpaRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException(productId + "에 해당하는 물품이 없습니다."))
                    .getMember();
            alertUtil.run(member.getId(), member.getNickname(), "새로운 입찰", AlertType.newBid);

            return collect;
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    //입찰 현황 조회
    @GetMapping("/{productId}")
    public ResponseEntity<List<BidResponseDto>> getBids(@PathVariable Long productId){
        List<BidResponseDto> collect = bidService.getBidsForProduct(productId);
        return ResponseEntity.ok(collect);
    }

    @GetMapping("/member")
    public ResponseEntity<List<MemberBidListResponseDto>> getMemberBids(@RequestHeader("Authorization") String authorization){
        Long memberId = jwtUtil.extractMemberId(authorization);
        List<MemberBidListResponseDto> collect = bidService.getMemberBid(memberId);

        return ResponseEntity.ok(collect);
    }
}
