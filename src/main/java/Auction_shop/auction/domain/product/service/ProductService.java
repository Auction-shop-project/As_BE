package Auction_shop.auction.domain.product.service;

import Auction_shop.auction.domain.product.Product;
import Auction_shop.auction.domain.product.ProductDocument;
import Auction_shop.auction.web.dto.product.ProductDto;
import Auction_shop.auction.web.dto.product.ProductListResponseDto;
import Auction_shop.auction.web.dto.product.ProductResponseDto;
import Auction_shop.auction.web.dto.product.ProductUpdateDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    Product save(ProductDto productDto, Long memberId, List<MultipartFile> images);
    Iterable<ProductDocument> findAllProduct(Long memberId);
    Iterable<ProductDocument> findAllByNickname(String nickname);
    Iterable<ProductDocument> findByTitle(String title);
    Product findProductById(Long memberId, Long product_id);
    Product updateProductById(ProductUpdateDto productUpdateDto, Long product_id, List<MultipartFile> images);
    int findCurrentPriceById(Long productId);
    void purchaseProductItem(Long product_id);
    boolean deleteProductById(Long product_id);
    void updateCreateBy(String oldNickname, String newNickname, Long memberId);
    void updateProductPrices();
    void createDummyProducts(int count);
}
