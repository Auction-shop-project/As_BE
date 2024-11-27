package Auction_shop.auction.domain.like;

import Auction_shop.auction.util.BaseEntity;
import Auction_shop.auction.domain.member.Member;
import Auction_shop.auction.domain.product.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "likes")
public class Like extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public void setMember(Member member){
        this.member = member;
    }

    public void setProduct(Product product){
        this.product = product;
    }
}
