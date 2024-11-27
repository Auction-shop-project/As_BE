package Auction_shop.auction.domain.block.controller;

import Auction_shop.auction.domain.block.Block;
import Auction_shop.auction.domain.block.service.BlockService;
import Auction_shop.auction.security.jwt.JwtUtil;
import Auction_shop.auction.web.dto.block.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/block")
public class BlockController {
    private final BlockService blockService;
    private final BlockMapper blockMapper;
    private final JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<BlockListResponseDto> getList(@RequestHeader("Authorization") String authorization){
        Long memberId = jwtUtil.extractMemberId(authorization);
        List<Block> blocks = blockService.getList(memberId);
        BlockListResponseDto collect = blockMapper.toListResponseDto(blocks);
        return ResponseEntity.ok(collect);
    }

    @PostMapping
    public ResponseEntity<BlockResponseDto> create(@RequestHeader("Authorization") String authorization,
                                                        @RequestBody BlockCreateRequestDto blockCreateRequestDto){
        Long memberId = jwtUtil.extractMemberId(authorization);
        Block block = blockService.create(memberId, blockCreateRequestDto.getBlockedMemberId());
        BlockResponseDto collect = blockMapper.toResponseDto(block);
        return ResponseEntity.status(HttpStatus.CREATED).body(collect);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestBody BlockDeleteRequestDto blockDeleteRequestDto){
        blockService.delete(blockDeleteRequestDto.getId());
        return ResponseEntity.noContent().build();
    }
}
