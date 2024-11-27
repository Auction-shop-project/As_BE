package Auction_shop.auction.domain.bid;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long productId;
    private Long memberId;
    private Long paymentId;
    private int amount;
    private LocalDateTime bidTime;

    @Enumerated(EnumType.STRING)
    private BidStatus bidStatus;

    public void changeStatus(BidStatus bidStatus) {
        this.bidStatus = bidStatus;
    }
}
