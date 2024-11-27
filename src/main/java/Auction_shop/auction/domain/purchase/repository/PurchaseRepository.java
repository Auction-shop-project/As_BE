package Auction_shop.auction.domain.purchase.repository;

import Auction_shop.auction.domain.product.Product;
import Auction_shop.auction.domain.purchase.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findByMemberId(Long memberId);
}
