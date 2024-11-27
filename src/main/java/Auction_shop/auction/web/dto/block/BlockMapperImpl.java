package Auction_shop.auction.web.dto.block;

import Auction_shop.auction.domain.block.Block;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BlockMapperImpl implements BlockMapper {

    @Override
    public BlockResponseDto toResponseDto(Block block) {
        BlockResponseDto responseDto = new BlockResponseDto().builder()
                .id(block.getId())
                .BlockedMemberId(block.getBlocked().getId())
                .BlockedMemberName(block.getBlocked().getName())
                .BlockedMemberPhone(block.getBlocked().getPhone())
                .BlockedMemberAddresses(block.getBlocked().getAddresses()).
                build();
        return responseDto;
    }

    @Override
    public BlockListResponseDto toListResponseDto(List<Block> blocks) {
        BlockListResponseDto toListResponseDto = new BlockListResponseDto().builder()
                .list(blocks.stream().map(b -> toResponseDto(b)).toList())
                .build();
        return toListResponseDto;
    }
}
