package Auction_shop.auction.util.product;

import Auction_shop.auction.domain.product.Product;
import Auction_shop.auction.domain.product.ProductDocument;
import Auction_shop.auction.domain.product.repository.ProductElasticsearchRepository;
import Auction_shop.auction.domain.product.repository.ProductJpaRepository;
import Auction_shop.auction.web.dto.product.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductDummyMaker {

    private ProductJpaRepository productJpaRepository;
    private ProductElasticsearchRepository productElasticsearchRepository;
    private ProductMapper productMapper;

    private final Random random = new Random();

    @Transactional
    public void createDummyProducts(int count) {
        for (int i = 0; i < count; i++) {
            Product product = Product.builder()
                    .title("Product " + (i + 1))
                    .conditions("New")
                    .categories(createRandomCategories())
                    .tradeTypes(createRandomTradeTypes())
                    .tradeLocation("Location " + random.nextInt(100))
                    .initial_price(random.nextInt(1000) + 100) // 최소 100
                    .minimum_price(random.nextInt(500) + 50) // 최소 50
                    .startTime(LocalDateTime.now().minusDays(random.nextInt(10)))
                    .imageList(null)
                    .endTime(LocalDateTime.now().plusDays(random.nextInt(10)))
                    .updateTime(LocalDateTime.now())
                    .isSold(false)
                    .details("This is a description for product " + (i + 1))
                    .build();

            productJpaRepository.save(product);
            ProductDocument document = productMapper.toDocument(product);
            productElasticsearchRepository.save(document);
        }
    }

    private Set<String> createRandomCategories() {
        Set<String> categories = new HashSet<>();
        categories.add("검색");
        categories.add("테스트");
        categories.add("용");
        categories.add("더미");
        categories.add("데이터");
        return categories;
    }

    private Set<String> createRandomTradeTypes() {
        Set<String> tradeTypes = new HashSet<>();
        tradeTypes.add("Sell");
        tradeTypes.add("Trade");
        return tradeTypes;
    }
}
