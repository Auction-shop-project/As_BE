package Auction_shop.auction.product.service;

import Auction_shop.auction.product.dto.ProductDto;
import Auction_shop.auction.product.dto.ProductListResponseDto;
import Auction_shop.auction.product.dto.ProductResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    ProductResponseDto save(ProductDto productDto, Long memberId, List<MultipartFile> images);
    List<ProductListResponseDto> findAllProduct();
    List<ProductListResponseDto> findAllByMemberId(Long MemberId);
    ProductResponseDto findProductById(Long product_id);
    ProductResponseDto updateProductById(ProductDto productDto, Long product_id, List<MultipartFile> images);
    boolean deleteProductById(Long product_id);
    void updateProductPrices();
}
