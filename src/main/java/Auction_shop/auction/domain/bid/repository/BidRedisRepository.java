package Auction_shop.auction.domain.bid.repository;

import Auction_shop.auction.domain.bid.Bid;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class BidRedisRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    // Bid 객체를 해시 구조로 저장
    public void save(Bid bid) {
        String bidKey = "bid:" + bid.getId(); // Bid의 고유 키
        try {
            String jsonBid = objectMapper.writeValueAsString(bid); // Bid 객체를 JSON 문자열로 변환
            redisTemplate.opsForHash().put(bidKey, "bidData", jsonBid); // 해시에 저장

            // 제품 및 멤버에 대한 Bid ID를 관리
            String productKey = "bids:product:" + bid.getProductId();
            redisTemplate.opsForList().rightPush(productKey, bid.getId().toString()); // 제품 ID별 Bid ID 저장

            String memberKey = "bids:member:" + bid.getMemberId();
            redisTemplate.opsForList().rightPush(memberKey, bid.getId().toString()); // 멤버 ID별 Bid ID 저장
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // 예외 처리
        }
    }

    // 제품 ID로 Bid 목록 가져오기
    public List<Bid> findBidsByProductId(Long productId) {
        String key = "bids:product:" + productId; // 제품 ID에 대한 Bid ID 리스트 키
        List<String> bidIds = redisTemplate.opsForList().range(key, 0, -1); // Bid ID 리스트 가져오기

        return bidIds.stream()
                .map(bidId -> {
                    String jsonBid = (String) redisTemplate.opsForHash().get("bid:" + bidId, "bidData"); // 해시에서 Bid 데이터 가져오기
                    try {
                        return objectMapper.readValue(jsonBid, Bid.class); // JSON 문자열을 Bid 객체로 변환
                    } catch (JsonProcessingException e) {
                        e.printStackTrace(); // 예외 처리
                        return null; // 변환 실패 시 null 반환
                    }
                })
                .collect(Collectors.toList());
    }

    // 멤버 ID로 Bid 목록 가져오기
    public List<Bid> findBidsByMemberId(Long memberId) {
        String key = "bids:member:" + memberId; // 멤버 ID에 대한 Bid ID 리스트 키
        List<String> bidIds = redisTemplate.opsForList().range(key, 0, -1); // Bid ID 리스트 가져오기

        return bidIds.stream()
                .map(bidId -> {
                    String jsonBid = (String) redisTemplate.opsForHash().get("bid:" + bidId, "bidData"); // 해시에서 Bid 데이터 가져오기
                    try {
                        return objectMapper.readValue(jsonBid, Bid.class); // JSON 문자열을 Bid 객체로 변환
                    } catch (JsonProcessingException e) {
                        e.printStackTrace(); // 예외 처리
                        return null; // 변환 실패 시 null 반환
                    }
                })
                .collect(Collectors.toList());
    }

    // Bid 업데이트
    public void updateBidInRedis(Bid bid) {
        String bidKey = "bid:" + bid.getId(); // Bid의 고유 키
        try {
            String jsonBid = objectMapper.writeValueAsString(bid); // Bid 객체를 JSON 문자열로 변환
            redisTemplate.opsForHash().put(bidKey, "bidData", jsonBid); // 해시에서 업데이트
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // 예외 처리
        }
    }

    //미사용 함수 ) 추후에 사용 할 시 주석 해제
//    public Bid findHighestBidByProductId(Long productId) {
//        String key = "bids" + productId;
//
//        Long size = redisTemplate.opsForList().size(key);
//
//        if (size == null || size == 0) {
//            return null; // 리스트가 비어있으면 null 반환
//        }
//
//        String latestJsonBid = redisTemplate.opsForList().index(key, size - 1);
//
//        if (latestJsonBid == null) {
//            return null; // 없으면 null 반환
//        }
//
//        try {
//            return objectMapper.readValue(latestJsonBid, Bid.class); // JSON 문자열을 Bid 객체로 변환
//        } catch (JsonProcessingException e) {
//            e.printStackTrace(); // 예외 처리
//            return null; // 변환 실패 시 null 반환
//        }
//    }
}
