package Auction_shop.auction.domain.priceChange.service;

import Auction_shop.auction.domain.priceChange.PriceChange;
import Auction_shop.auction.domain.priceChange.repository.PriceChangeJpaRepository;
import Auction_shop.auction.domain.priceChange.repository.PriceChangeRedisRepository;
import Auction_shop.auction.web.dto.PriceChange.PriceChangeMapper;
import Auction_shop.auction.web.dto.PriceChange.PriceChangeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PriceChangeService {

    private final PriceChangeJpaRepository priceChangeJpaRepository;
    private final PriceChangeRedisRepository priceChangeRedisRepository;
    private final PriceChangeMapper priceChangeMapper;

    public void savePriceChange(PriceChange priceChange){
        priceChangeJpaRepository.save(priceChange);
        priceChangeRedisRepository.save(priceChange);
    }

    public List<PriceChangeResponseDto> getPriceChangeForProduct(Long productId){
        List<PriceChange> priceChanges = priceChangeRedisRepository.findPriceChangesByProductId(productId);

        if(priceChanges.isEmpty()){
            priceChanges = priceChangeJpaRepository.findByProductId(productId);
            for(PriceChange priceChange : priceChanges){
                priceChangeRedisRepository.save(priceChange);
            }
        }

        List<PriceChangeResponseDto> collect = priceChanges.stream()
                .map(priceChangeMapper::toResponseDto)
                .collect(Collectors.toList());

        Collections.reverse(collect);

        return collect;
    }

    public int getChangeOrder(Long productId){
        return (int)priceChangeJpaRepository.countByProductId(productId) + 1;
    }
}
