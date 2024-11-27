package Auction_shop.auction.domain.priceChange.repository;

import Auction_shop.auction.domain.priceChange.PriceChange;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class PriceChangeRedisRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public void save(PriceChange priceChange){
        String key = "price_change:" + priceChange.getProductId();
        try {
            String jsonPriceChange = objectMapper.writeValueAsString(priceChange);
            redisTemplate.opsForList().rightPush(key, jsonPriceChange);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public List<PriceChange> findPriceChangesByProductId(Long productId){
        String key = "price_change" + productId;
        List<String> jsonPriceChanges = redisTemplate.opsForList().range(key, 0, -1);

        return jsonPriceChanges.stream()
                .map(this::convertJsonToPriceChange)
                .collect(Collectors.toList());
    }

    private PriceChange convertJsonToPriceChange(String json) {
        try {
            return objectMapper.readValue(json, PriceChange.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
