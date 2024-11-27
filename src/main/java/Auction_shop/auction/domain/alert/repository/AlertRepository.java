package Auction_shop.auction.domain.alert.repository;

import Auction_shop.auction.domain.alert.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findByMemberId(Long memberId);
}
