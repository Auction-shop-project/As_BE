package Auction_shop.auction.domain.member.repository;

import Auction_shop.auction.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
