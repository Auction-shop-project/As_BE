package Auction_shop.auction.domain.product.controller;

import Auction_shop.auction.domain.like.service.LikeService;
import Auction_shop.auction.domain.product.Product;
import Auction_shop.auction.domain.product.ProductDocument;
import Auction_shop.auction.security.jwt.JwtUtil;
import Auction_shop.auction.web.dto.product.*;
import Auction_shop.auction.domain.product.service.ProductService;
import Auction_shop.auction.domain.product.validation.ProductValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@Slf4j
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final LikeService likeService;
    private final ProductMapper productMapper;
    private final ProductValidator productValidator;
    private final JwtUtil jwtUtil;

    @Autowired
    public ProductController(ProductService productService, LikeService likeService, ProductValidator productValidator, ProductMapper productMapper, JwtUtil jwtUtil) {
        this.productService = productService;
        this.likeService = likeService;
        this.productValidator = productValidator;
        this.productMapper = productMapper;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 상품 등록
     */
    @PostMapping("/registration")
    public ResponseEntity<Object> createProduct(@RequestHeader("Authorization") String authorization,
            @RequestPart(value = "product") ProductDto productDto,
            @RequestPart(value = "images", required = false) final List<MultipartFile> images,
            BindingResult bindingResult) {
        productValidator.validate(productDto, bindingResult);
        log.info("bindingResult={}", bindingResult);

        if (bindingResult.hasErrors()) {
            // 클라이언트 에러 400
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong Type : " + bindingResult.getFieldError().getDefaultMessage());
        }
        Long memberId = jwtUtil.extractMemberId(authorization);
        try {
            Product product = productService.save(productDto, memberId, images);
            ProductResponseDto responseDto = productMapper.toResponseDto(product);
            responseDto.setIsOwner(true);
            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        } catch (Exception e) {
            // 서버 에러 500
            log.info("error={}", e.getMessage(), ProductController.class);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error");
        }
    }

    /**
     * 상품 조회
     */
    @GetMapping()
    public ResponseEntity<Object> getAllProduct(@RequestHeader("Authorization") String authorization){
        Long memberId = jwtUtil.extractMemberId(authorization);
        Iterable<ProductDocument> products = productService.findAllProduct(memberId);
        List<Long> likedProductsIds = likeService.getLikeItems(memberId);

        List<ProductListResponseDto> collect = StreamSupport.stream(products.spliterator(), false)
                .map(product -> productMapper.toListResponeDto(product, likedProductsIds.contains(product.getId())))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(collect);
    }

    /**
     * 상품 이름 검색
     */
    @GetMapping("/{title}")
    public ResponseEntity<Object> getByTitle(@PathVariable String title){
        return ResponseEntity.status(HttpStatus.OK).body(productService.findByTitleLike(title));
    }

    /**
     * NEW 경매 추천
     */
    @GetMapping("/new")
    public ResponseEntity<Object> getNewAuctions() {
        List<ProductDocument> products = productService.getNewProducts();

        List<ProductRecommendedDto> collect = products.stream()
                .map(product -> productMapper.toRecommendedDto(product))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(collect);
    }

    /**
     * HOT 경매 추천 (좋아요 순)
     */
    @GetMapping("/hot")
    public ResponseEntity<Object> getHotAuctions() {
        List<ProductDocument> products = productService.getHotProducts();

        List<ProductRecommendedDto> collect = products.stream()
                .map(product -> productMapper.toRecommendedDto(product))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(collect);
    }

    /**
     * 상품 상세 조회
     */
    @GetMapping("/search/{product_id}")
    public ResponseEntity<Object> getProductById(@RequestHeader("Authorization") String authorization, @PathVariable Long product_id) {
        Long memberId = jwtUtil.extractMemberId(authorization);
        String name = jwtUtil.extractNickname(authorization);
        Product product = productService.findProductById(memberId, product_id);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error - Product not found, product_id doesn't exist in Database :(");
        }
        System.out.println("memberId = " + memberId);
        System.out.println("name = " + name);
        boolean isLiked = likeService.isLiked(memberId, product_id);
        boolean isOwner = product.getCreatedBy().equals(name);
        ProductResponseDto responseDto = productMapper.toResponseDto(product);
        responseDto.setIsOwner(isOwner);
        responseDto.setIsLiked(isLiked);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    /**
     * 상품 수정
     */
    @PutMapping("/update/{product_id}")
    public ResponseEntity<Object> updateProductById(
            @PathVariable Long product_id, @RequestPart(value = "product") ProductUpdateDto productUpdateDto,
            @RequestPart(value = "images", required = false) final List<MultipartFile> images) {
        Product product = productService.updateProductById(productUpdateDto, product_id, images);
        ProductResponseDto responseDto = productMapper.toResponseDto(product);
        responseDto.setIsOwner(true);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }


    /**
     * 상품 삭제
     */
    @DeleteMapping("/delete/{product_id}")
    public ResponseEntity<Object> deleteProductById(@PathVariable Long product_id) {
        boolean isFound = productService.deleteProductById(product_id);
        if (isFound) {
            return ResponseEntity.status(HttpStatus.OK).body("delete success");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Delete failed. check product_id and Database");
        }
    }
}