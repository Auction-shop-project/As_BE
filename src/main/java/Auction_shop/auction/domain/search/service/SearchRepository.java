package Auction_shop.auction.domain.search.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchRepository {

    private final RedisTemplate<String, String> redisTemplate;

    private static final int MAX_SEARCH_TERMS = 10; // 최대 검색어 수

    public void saveSearchTerm(Long memberId, String searchTerm) {
        String redisKey = "recent_searches:" + memberId;

        // 검색어 추가
        redisTemplate.opsForList().rightPush(redisKey, searchTerm);

        // 리스트의 최대 길이 설정 (예: 최근 10개 유지)
        redisTemplate.opsForList().trim(redisKey, -MAX_SEARCH_TERMS, -1);
    }

    public List<String> getRecentSearches(Long memberId) {
        String redisKey = "recent_searches:" + memberId;
        // 최대 10개 항목을 가져옴
        return redisTemplate.opsForList().range(redisKey, 0, 9); // 0부터 9까지 조회
    }
}
