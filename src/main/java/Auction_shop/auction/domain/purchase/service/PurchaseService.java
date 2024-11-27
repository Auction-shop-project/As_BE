package Auction_shop.auction.domain.purchase.service;

import Auction_shop.auction.domain.product.Product;
import Auction_shop.auction.domain.purchase.Purchase;
import Auction_shop.auction.domain.purchase.repository.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;

    public List<Purchase> getPurchasesByMemberId(Long memberId) {
        return purchaseRepository.findByMemberId(memberId);
    }
    public Purchase createPurchase(Purchase purchase) {
        return purchaseRepository.save(purchase);
    }
}
