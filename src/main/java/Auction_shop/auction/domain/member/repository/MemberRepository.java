package Auction_shop.auction.domain.member.repository;

import Auction_shop.auction.domain.member.Member;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUuid(String uuid);

    boolean existsByNickname(String nickname);

    @Query("SELECT m FROM Member m ORDER BY m.point DESC")
    List<Member> findTop3ByOrderByPointDesc();
}
