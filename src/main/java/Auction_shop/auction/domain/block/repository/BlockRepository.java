package Auction_shop.auction.domain.block.repository;

import Auction_shop.auction.domain.block.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BlockRepository extends JpaRepository<Block, Long> {
    List<Block> findByBlockerId(Long blockerId);
}
