package Auction_shop.auction.web.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateDto {

    private String title;
    private String details;
    private Set<String> categories;
    private Set<String> tradeType;
    private String tradeLocation;
    private String conditions;
    private List<String> imageUrlsToKeep;
}
