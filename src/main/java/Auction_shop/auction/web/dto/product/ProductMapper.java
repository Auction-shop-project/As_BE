package Auction_shop.auction.web.dto.product;

import Auction_shop.auction.domain.member.Member;
import Auction_shop.auction.domain.product.Product;
import Auction_shop.auction.domain.product.ProductDocument;
import Auction_shop.auction.domain.purchase.Purchase;

public interface ProductMapper {

    ProductResponseDto toResponseDto(Product product);
    ProductDocument toDocument(Product product);
    ProductListResponseDto toListResponseDto(ProductDocument productDocument, boolean isLiked);
    ProductRecommendedDto toRecommendedDto(ProductDocument productDocument);
    Product toEntity(ProductDto productDto, Member member);
    ProductPurchaseListDto purchaseToListResponseDto(Purchase purchase);
}
