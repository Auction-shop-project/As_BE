package Auction_shop.auction.web.dto.product;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProductResponseDto {
    private Long product_id;         // 제품ID
    private String title;           // 판매글 제목
    private String product_type;    // 제품 분류
    private String conditions;       // 제품 상태
    private Set<String> categories;         // 카테고리
    private Set<String> tradeTypes;           // 거래 방식
    private String tradeLocation;   // 직거래 희망 장소
    private int likeCount;          // 좋아요 갯수
    private int initial_price;       // 시작 가격
    private int minimum_price;      // 최저 가격
    private int current_price;      // 현재 가격
    private boolean isSold;         // 판매 여부
    private boolean isLiked;        // 좋아요 여부
    private String createdBy;       // 등록자 닉네임

    private LocalDateTime startTime; // 경매 시작 시간
    private LocalDateTime endTime;   // 경매 종료 시간

    private String details;         // 설명
    private List<String> imageUrls;     //이미지 url


}
