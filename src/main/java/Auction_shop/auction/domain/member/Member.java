package Auction_shop.auction.domain.member;

import Auction_shop.auction.domain.BaseEntity;
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
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, length = 20)
    private String username;

    @Column(nullable = false, length = 10)
    private String name;

    @Embedded
    private Address address;

    @Column(nullable = false, length = 13)
    private String phone;

    @Column(nullable = false)
    private Long point;
}
