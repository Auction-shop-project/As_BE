package Auction_shop.auction.domain.image.repository;

import Auction_shop.auction.domain.image.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
    void deleteByStoredName(String storedName);
}
