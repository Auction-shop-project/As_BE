package Auction_shop.auction.domain.priceChange.repository;

import Auction_shop.auction.domain.priceChange.PriceChange;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PriceChangeJpaRepository extends JpaRepository<PriceChange, Long> {
    List<PriceChange> findByProductId(Long productId);
    long countByProductId(Long productId);
}
