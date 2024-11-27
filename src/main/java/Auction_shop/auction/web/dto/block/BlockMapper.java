package Auction_shop.auction.web.dto.block;

import Auction_shop.auction.domain.block.Block;
import java.util.List;

public interface BlockMapper {

    BlockResponseDto toResponseDto(Block block);
    BlockListResponseDto toListResponseDto(List<Block> blocks);
}
