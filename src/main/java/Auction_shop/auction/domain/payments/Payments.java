package Auction_shop.auction.domain.payments;

import Auction_shop.auction.domain.member.Member;
import Auction_shop.auction.domain.product.Product;
import Auction_shop.auction.util.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payments extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String paymentMethod;
    private int amount;
    private String merchantUid;
    private String impUid;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne // 상품과 다대일 관계
    @JoinColumn(name = "product_id") // 결제가 연관된 상품
    private Product product;
}
