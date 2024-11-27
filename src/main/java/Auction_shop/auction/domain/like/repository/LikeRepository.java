package Auction_shop.auction.domain.like.repository;

import Auction_shop.auction.domain.like.Like;
import Auction_shop.auction.domain.member.Member;
import Auction_shop.auction.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {

    List<Like> findByMember(Member member);
    Like findByMemberAndProduct(Member member, Product product);
}
