package Auction_shop.auction.domain.product.service;

import Auction_shop.auction.domain.product.Product;
import Auction_shop.auction.domain.product.ProductDocument;
import Auction_shop.auction.web.dto.product.ProductDto;
import Auction_shop.auction.web.dto.product.ProductUpdateDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    //물건 등록
    Product save(ProductDto productDto, Long memberId, List<MultipartFile> images);

    //물건 찾기
    Iterable<ProductDocument> findAllProduct(Long memberId);
    Iterable<ProductDocument> findAllByNickname(String nickname);
    Iterable<ProductDocument> findByTitleLike(String title);

    //물건 추천
    List<ProductDocument> getNewProducts();
    List<ProductDocument> getHotProducts();

    //물건 세부 조회
    Product findProductById(Long memberId, Long product_id);
    Product updateProductById(ProductUpdateDto productUpdateDto, Long product_id, List<MultipartFile> images);

    //물건 ID로 현재 가격 찾기
    int findCurrentPriceById(Long productId);

    //물건 구매
    void purchaseProductItem(Long product_id);

    //물건 삭제
    boolean deleteProductById(Long product_id);

    //닉네임 변경후 물건 생성자명 변경
    void updateCreateBy(String oldNickname, String newNickname, Long memberId);

    void updateProductPrices();
    void createDummyProducts(int count);
}
