package Auction_shop.auction.domain.block.service;

import Auction_shop.auction.domain.block.Block;
import Auction_shop.auction.domain.block.repository.BlockRepository;
import Auction_shop.auction.domain.member.Member;
import Auction_shop.auction.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BlockService {
    private final BlockRepository blockRepository;
    private final MemberRepository memberRepository;

    public List<Block> getList(Long memberId) {
        return blockRepository.findByBlockerId(memberId);
    }

    @Transactional
    public Block create(Long blockerId, Long blockedId) {
        if(blockerId == blockedId) new RuntimeException("자기 자신을 차단할 수 없습니다.");

        Member blocker = memberRepository.findById(blockerId).orElseThrow();
        Member blocked = memberRepository.findById(blockedId).orElseThrow();

        Block block = Block.builder()
                .blocker(blocker)
                .blocked(blocked)
                .build();

        blockRepository.save(block);

        return block;
    }

    @Transactional
    public void delete(Long blockId) {
        blockRepository.deleteById(blockId);
    }
}
