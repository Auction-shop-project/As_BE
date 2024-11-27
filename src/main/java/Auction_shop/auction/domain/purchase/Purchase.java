package Auction_shop.auction.domain.purchase;

import Auction_shop.auction.domain.member.Member;
import Auction_shop.auction.domain.product.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(indexes = {
        @Index(name = "idx_member_id", columnList = "member_id"),
        @Index(name = "idx_product_id", columnList = "product_id")
})
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_id")
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @OneToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private LocalDateTime purchaseDate;
}