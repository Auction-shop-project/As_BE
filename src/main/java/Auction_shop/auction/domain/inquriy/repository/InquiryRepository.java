package Auction_shop.auction.domain.inquriy.repository;

import Auction_shop.auction.domain.inquriy.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    List<Inquiry> findByMemberId(Long memberId);
    List<Inquiry> findByStatus(boolean status);

}
