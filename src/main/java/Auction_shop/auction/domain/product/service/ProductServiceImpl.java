package Auction_shop.auction.domain.product.service;

import Auction_shop.auction.domain.bid.Bid;
import Auction_shop.auction.domain.bid.BidStatus;
import Auction_shop.auction.domain.bid.repository.BidRedisRepository;
import Auction_shop.auction.domain.bid.service.BidService;
import Auction_shop.auction.domain.image.Image;
import Auction_shop.auction.domain.image.service.ImageService;
import Auction_shop.auction.domain.member.Member;
import Auction_shop.auction.domain.member.repository.MemberRepository;
import Auction_shop.auction.domain.priceChange.PriceChange;
import Auction_shop.auction.domain.priceChange.service.PriceChangeService;
import Auction_shop.auction.domain.product.ProductDocument;
import Auction_shop.auction.domain.product.ProductType;
import Auction_shop.auction.domain.product.repository.ProductElasticsearchRepository;
import Auction_shop.auction.domain.product.repository.ProductJpaRepository;
import Auction_shop.auction.domain.product.Product;
import Auction_shop.auction.domain.purchase.Purchase;
import Auction_shop.auction.domain.purchase.service.PurchaseService;
import Auction_shop.auction.web.dto.product.*;
import Auction_shop.auction.web.fcm.NotificationType;
import Auction_shop.auction.web.fcm.service.FirebaseCloudMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductJpaRepository productJpaRepository;
    private final ProductElasticsearchRepository productElasticsearchRepository;
    private final MemberRepository memberRepository;
    private final PriceChangeService priceChangeService;
    private final BidService bidService;
    private final PurchaseService purchaseService;
    private final ProductMapper productMapper;
    private final ImageService imageService;
    private final BidRedisRepository bidRedisRepository;
    private final FirebaseCloudMessageService fcmService;

    @Override
    @Transactional
    public Product save(ProductDto productDto, Long memberId, List<MultipartFile> images) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow();

        Product product = productMapper.toEntity(productDto, member);
        member.addProduct(product);

        List<Image> imageList = imageService.saveImages(images);
        product.setImageList(imageList);

        Product savedProduct = productJpaRepository.save(product);

        ProductDocument productDocument = productMapper.toDocument(savedProduct);
        productElasticsearchRepository.save(productDocument);

        return savedProduct;
    }

    @Override
    public Iterable<ProductDocument> findAllProduct(Long memberId) {
        return productElasticsearchRepository.findAll();
    }

    @Override
    public Iterable<ProductDocument> findAllByNickname(String nickname) {
        return productElasticsearchRepository.findByCreatedBy(nickname);
    }

    @Override
    public Iterable<ProductDocument> findByTitleLike(String title) {
        return productElasticsearchRepository.findByTitleLike(title);
    }

    @Override
    public List<ProductDocument> getUserCategoryProducts(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow();
        Set<String> categories = member.getCategories();
        List<ProductDocument> products = productElasticsearchRepository.findByCategoriesIn(categories);
        if (!products.isEmpty()) {
            int numberOfElementsToReturn = Math.min(products.size(), 5);
            return getRandomElements(products, numberOfElementsToReturn);
        }
        return null;
    }

    @Override
    public List<ProductDocument> getNewProducts() {
        List<ProductDocument> products = productElasticsearchRepository.findTop20ByOrderByCreatedAtDesc();
        if (!products.isEmpty()) {
            int numberOfElementsToReturn = Math.min(products.size(), 5);
            return getRandomElements(products, numberOfElementsToReturn);
        }
        return null;
    }

    @Override
    public List<ProductDocument> getHotProducts() {
        List<ProductDocument> products = productElasticsearchRepository.findTop20ByOrderByLikeCountDesc();
        if (!products.isEmpty()) {
            int numberOfElementsToReturn = Math.min(products.size(), 5);
            return getRandomElements(products, numberOfElementsToReturn);
        }
        return null;
    }

    @Override
    public Iterable<ProductDocument> getProductsFromTop5Members() {
        List<Member> topMembers = memberRepository.findTop3ByOrderByPointDesc();
        List<Long> memberIds = topMembers.stream().map(Member::getId).collect(Collectors.toList());
        return productElasticsearchRepository.findByMemberIdIn(memberIds);
    }

    @Override
    public Product findProductById(Long product_id) {
        Product product = productJpaRepository.findById(product_id)
                .orElseThrow(() -> new IllegalArgumentException(product_id + "에 해당하는 물건이 없습니다."));
        return product;
    }

    @Override
    public ProductType findProductTypeById(Long productId) {
        ProductType productType = productJpaRepository.findProductTypeById(productId);
        return productType;
    }

    @Override
    @Transactional
    public Product updateProductById(ProductUpdateDto productUpdateDto, Long product_id, List<MultipartFile> images) {
        Product product = productJpaRepository.findById(product_id)
                .orElseThrow(() -> new IllegalArgumentException(product_id + "에 해당하는 물건이 없습니다."));

        List<String> existingImageUrls = product.getImageUrls();

        List<String> urlsToRetain = productUpdateDto.getImageUrlsToKeep();

        List<String> urlsToDelete = existingImageUrls.stream()
                .filter(url -> !urlsToRetain.contains(url))
                .collect(Collectors.toList());

        deleteExistingImages(product, urlsToDelete);

        List<Image> imageList = imageService.saveImages(images);
        product.getImageList().addAll(imageList);

        product.updateProduct(productUpdateDto.getTitle(), productUpdateDto.getCategories(), productUpdateDto.getTradeType(), productUpdateDto.getDetails(), productUpdateDto.getTradeLocation(), productUpdateDto.getConditions());

        ProductDocument productDocument = productMapper.toDocument(product);
        productElasticsearchRepository.save(productDocument);

        return product;
    }

    @Override
    @Transactional
    public void updateCreateBy(String oldNickname, String newNickname, Long memberId) {
        List<Product> products = productJpaRepository.findAllByMemberId(memberId);
        List<ProductDocument> productDocuments = new ArrayList<>();

        for (Product product : products) {
            product.updateCreatedBy(newNickname);
            ProductDocument productDocument = productMapper.toDocument(product);
            productDocuments.add(productDocument);
        }

        // Elasticsearch 배치 저장
        try {
            productElasticsearchRepository.saveAll(productDocuments);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void purchaseProductItem(Long product_id, Long memberId) {
        Product product = productJpaRepository.findById(product_id)
                .orElseThrow(() -> new IllegalArgumentException(product_id + "에 해당하는 물건이 없습니다."));

        if (product.isSold()) {
            throw new RuntimeException("이미 판매된 물품입니다.");
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException(memberId + "에 해당하는 유저가 없습니다."));

        member.addPoint(1000L);

        Purchase purchase = Purchase.builder()
                .memberId(memberId)
                .product(product)
                .purchaseDate(LocalDateTime.now())
                .build();
        purchaseService.createPurchase(purchase);

        product.setIsSold(true);
        productJpaRepository.save(product);

        ProductDocument productDocument = productMapper.toDocument(product);
        productElasticsearchRepository.save(productDocument);
    }

    //추후 알림 보내는 메서드 진행 중 속도 저하 개선 필수
    @Override
    @Scheduled(fixedRate = 10000)
    @Transactional
    public void checkProductToEnd() throws IOException {
        LocalDateTime currentTime = LocalDateTime.now();
        int pageSize = 2000;
        int pageNumber = 0;

        Page<Product> page;
        do {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            page = productJpaRepository.findExpiredProduct(currentTime, ProductType.ASCENDING, pageable); // 만료된 제품 조회
            List<Product> updatedProducts = new ArrayList<>();
            for (Product product : page.getContent()) {
                product.setIsSold(true); // 경매 종료 처리
                Bid highestBid = bidService.getHighestBidForProduct(product.getId());
                if (highestBid != null) {
                    Long memberId = highestBid.getMemberId();

                    Member member = memberRepository.findById(memberId)
                            .orElseThrow(() -> new IllegalArgumentException(memberId + "에 해당하는 유저가 없습니다."));
                    member.addPoint(1000L);

                    Purchase purchase = Purchase.builder()
                            .memberId(memberId)
                            .product(product)
                            .purchaseDate(LocalDateTime.now())
                            .build();
                    purchaseService.createPurchase(purchase);
                    highestBid.changeStatus(BidStatus.SUCCESS);
                    bidRedisRepository.updateBidInRedis(highestBid);
                    //Todo 경매 우승자에게 알림 보내기 추가 부탁드립니다

                    //물건 올렸던 사람에게 푸시 알림
                    System.out.println("알림 받은 유저 = " + product.getMember().getId());
                    fcmService.sendMessageTo(product.getMember().getDeviceToken(),
                            "경매 종료!",
                            product.getTitle()+"의 경매가 끝났어요!",
                            product.getId(),
                            NotificationType.PRODUCT);

                    //경매 우승자에게 푸시 알림
                    System.out.println("알림 받은 유저 = " + memberId);
                    fcmService.sendMessageTo(member.getDeviceToken(),
                            "경매 종료!",
                            product.getTitle()+"의 입찰에 성공했어요!",
                            product.getId(),
                            NotificationType.PRODUCT);
                }
                updatedProducts.add(product);
            }
            saveUpdatedProducts(updatedProducts);
            pageNumber++;
        } while (page.hasNext());
    }

    @Override
    @Transactional
    public boolean deleteProductById(Long product_id) {
        boolean isFound = productJpaRepository.existsById(product_id);
        Product product = productJpaRepository.findById(product_id)
                .orElseThrow(() -> new IllegalArgumentException(product_id + "에 해당하는 물건이 없습니다."));
        if (isFound) {
            for (Image image : product.getImageList()) {
                imageService.deleteImage(image.getStoredName());
            }
            productJpaRepository.deleteById(product_id);
            productElasticsearchRepository.deleteById(product_id);
        }
        return isFound;
    }

    @Override
    public int findCurrentPriceById(Long productId) {
        int price = productJpaRepository.findCurrentPriceById(productId);
        return price;
    }

    //더미 10,000개로 확인 결과 성능에 영향은 없지만 추후 redis
    //하향식 경매 가격 내리기
    @Override
    @Scheduled(fixedRate = 10000)
    @Transactional
    public void updateProductPrices() {
        LocalDateTime currentTime = LocalDateTime.now();
        int pageSize = 2000;
        int pageNumber = 0;

        Page<Product> page;
        do {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            page = productJpaRepository.findActiveProduct(currentTime, ProductType.DESCENDING, pageable);

            List<Product> updatedProducts = new ArrayList<>();
            for (Product product : page.getContent()) {
                if (currentTime.isAfter(product.getUpdateTime().plusMinutes(1))) {
                    int newPrice = product.getCurrent_price() - (product.getInitial_price() / (int) product.getTotalHours());
                    if (newPrice < product.getMinimum_price()) {
                        newPrice = product.getMinimum_price();
                    }

                    if(newPrice != product.getCurrent_price()) {
                        PriceChange priceChange = PriceChange.builder()
                                .productId(product.getId())
                                .previousPrice(product.getCurrent_price())
                                .newPrice(newPrice)
                                .changeDate(currentTime)
                                .changeOrder(priceChangeService.getChangeOrder(product.getId()))
                                .build();

                        priceChangeService.savePriceChange(priceChange);

                        product.updateCurrentPrice(newPrice);
                        product.updateTime(currentTime);
                        updatedProducts.add(product);
                    }
                }
            }
            saveUpdatedProducts(updatedProducts);
            pageNumber++;
        } while (page.hasNext());
        System.out.println("수행 완료");
    }

    //성능 최적화를 위해 트랜잭션을 db 수정이 있을 때만 적용
    //범위가 너무 넓으면 락이 오래 유지되어 성능 저하
    @Transactional
    public void saveUpdatedProducts(List<Product> updatedProducts) {
        if (!updatedProducts.isEmpty()) {
            productJpaRepository.saveAll(updatedProducts);

            List<ProductDocument> productDocuments = updatedProducts.stream()
                    .map(productMapper::toDocument)
                    .collect(Collectors.toList());
            productElasticsearchRepository.saveAll(productDocuments);
        }
    }

    private void deleteExistingImages(Product product, List<String> urlsToDelete) {
        if (product.getImageUrls() != null) {
            for (String url : urlsToDelete) {
                Image imageToDelete = product.getImageList().stream()
                        .filter(image -> image.getAccessUrl().equals(url))
                        .findFirst()
                        .orElse(null);

                if (imageToDelete != null) {
                    imageService.deleteImage(imageToDelete.getStoredName());
                    product.getImageList().remove(imageToDelete);
                }
            }
        }
    }

    private List<ProductDocument> getRandomElements(List<ProductDocument> list, int number) {
        Random random = new Random();
        return random.ints(0, list.size())
                .distinct()
                .limit(number)
                .mapToObj(list::get)
                .collect(Collectors.toList());
    }
}