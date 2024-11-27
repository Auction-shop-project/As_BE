package Auction_shop.auction.domain.notice.repository;

import Auction_shop.auction.domain.notice.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
