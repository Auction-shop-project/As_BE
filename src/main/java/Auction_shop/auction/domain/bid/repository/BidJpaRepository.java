package Auction_shop.auction.domain.bid.repository;

import Auction_shop.auction.domain.bid.Bid;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidJpaRepository extends JpaRepository<Bid, Long> {

    List<Bid> findByProductId(Long productId);
    List<Bid> findByMemberId(Long memberId);
    Bid findBidByPaymentId(Long paymentId);
    Bid findTopByProductIdOrderByBidTimeDesc(Long productId);
}
