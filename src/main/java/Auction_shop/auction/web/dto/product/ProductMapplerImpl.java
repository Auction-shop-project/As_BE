package Auction_shop.auction.web.dto.product;

import Auction_shop.auction.domain.member.Member;
import Auction_shop.auction.domain.product.Product;
import Auction_shop.auction.domain.product.ProductDocument;
import Auction_shop.auction.domain.purchase.Purchase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductMapplerImpl implements ProductMapper{

    @Override
    public ProductResponseDto toResponseDto(Product product) {
        ProductResponseDto responseDto = ProductResponseDto.builder()
                .memberId(product.getMember().getId())
                .product_id(product.getId())
                .title(product.getTitle())
                .productType(product.getProductType())
                .conditions(product.getConditions())
                .categories(product.getCategories())
                .tradeTypes(product.getTradeTypes())
                .tradeLocation(product.getTradeLocation())
                .initial_price(product.getInitial_price())
                .current_price(product.getCurrent_price())
                .minimum_price(product.getMinimum_price())
                .createdBy(product.getCreatedBy())
                .startTime(product.getStartTime())
                .likeCount(product.getLikeCount())
                .endTime(product.getEndTime())
                .details(product.getDetails())
                .isSold(product.isSold())
                .imageUrls(product.getImageUrls())
                .bidCount(product.getBidCount())
                .build();

        return responseDto;
    }

    @Override
    public ProductListResponseDto toListResponseDto(ProductDocument productDocument, boolean isLiked) {

        ProductListResponseDto responseDto = ProductListResponseDto.builder()
                .product_id(productDocument.getId())
                .title(productDocument.getTitle())
                .productType(productDocument.getProductType())
                .conditions(productDocument.getConditions())
                .initial_price(productDocument.getInitialPrice())
                .categories(productDocument.getCategories())
                .tradeTypes(productDocument.getTradeTypes())
                .current_price(productDocument.getCurrentPrice())
                .tradeLocation(productDocument.getTradeLocation())
                .createdBy(productDocument.getCreatedBy())
                .likeCount(productDocument.getLikeCount())
                .isSold(productDocument.isSold())
                .imageUrl(productDocument.getImageUrl())
                .isLiked(isLiked)
                .bidCount(productDocument.getBidCount())
                .build();
        return responseDto;
    }

    @Override
    public ProductRecommendedDto toRecommendedDto(ProductDocument productDocument) {
        ProductRecommendedDto recommendedDto = ProductRecommendedDto.builder()
                .product_id(productDocument.getId())
                .title(productDocument.getTitle())
                .productType(productDocument.getProductType())
                .tradeTypes(productDocument.getTradeTypes())
                .initial_price(productDocument.getInitialPrice())
                .current_price(productDocument.getCurrentPrice())
                .imageUrl(productDocument.getImageUrl())
                .build();
        return recommendedDto;
    }

    @Override
    public Product toEntity(ProductDto productDto, Member member) {
        Product product = Product.builder()
                .title(productDto.getTitle())
                .productType(productDto.getProductType())
                .member(member)
                .conditions(productDto.getConditions())
                .categories(productDto.getCategories())
                .tradeTypes(productDto.getTradeTypes())
                .tradeLocation(productDto.getTradeLocation())
                .initial_price(productDto.getInitial_price())
                .startTime(productDto.getStartTime())
                .endTime(productDto.getEndTime())
                .updateTime(productDto.getStartTime())
                .isSold(false)
                .minimum_price(productDto.getMinimum_price())
                .details(productDto.getDetails())
                .bidCount(0)
                .build();
        return product;
    }

    @Override
    public ProductDocument toDocument(Product product) {
        ProductDocument productDocument = ProductDocument.builder()
                .id(product.getId())
                .memberId(product.getMember().getId())
                .title(product.getTitle())
                .productType(product.getProductType())
                .sold(product.isSold())
                .conditions(product.getConditions())
                .categories(product.getCategories())
                .tradeTypes(product.getTradeTypes())
                .tradeLocation(product.getTradeLocation())
                .initialPrice(product.getInitial_price())
                .currentPrice(product.getCurrent_price())
                .imageUrl(product.getImageUrls().stream().findFirst().orElse(null)) // 첫 번째 이미지 URL
                .createdBy(product.getCreatedBy())
                .createdAt(product.getCreatedAt())
                .likeCount(product.getLikeCount())
                .bidCount(product.getBidCount())
                .build();
        return productDocument;
    }

    @Override
    public ProductPurchaseListDto purchaseToListResponseDto(Purchase purchase) {
        Product product = purchase.getProduct();

        ProductPurchaseListDto responseDto = ProductPurchaseListDto.builder()
                .productId(product.getId())
                .title(product.getTitle())
                .productType(product.getProductType().name())
                .initial_price(product.getInitial_price())
                .current_price(product.getCurrent_price())
                .imageUrl(product.getImageUrls().stream().findFirst().orElse(null))
                .bidTime(purchase.getPurchaseDate())
                .build();
        return responseDto;
    }
}
