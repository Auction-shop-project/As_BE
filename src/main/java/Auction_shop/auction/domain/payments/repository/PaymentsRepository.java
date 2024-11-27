package Auction_shop.auction.domain.payments.repository;

import Auction_shop.auction.domain.payments.Payments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentsRepository extends JpaRepository<Payments, Long> {

    Optional<Payments> findTopByProductIdOrderByCreatedAtDesc(Long productId);
}
