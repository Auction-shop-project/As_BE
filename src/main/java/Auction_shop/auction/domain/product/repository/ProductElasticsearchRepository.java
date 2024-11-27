package Auction_shop.auction.domain.product.repository;

import Auction_shop.auction.domain.product.Product;
import Auction_shop.auction.domain.product.ProductDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface ProductElasticsearchRepository extends ElasticsearchRepository<ProductDocument, Long> {
    // 닉네임으로 제품 조회
    Iterable<ProductDocument> findByCreatedBy(String createdBy);

    @Query("{\"bool\": {\"must\": [{\"match\": {\"title\": {\"query\": \"?0\", \"operator\": \"and\"}}}, {\"term\": {\"sold\": false}}]}}}")
    Iterable<ProductDocument> findByTitleLike(@Param("title") String title);

    @Query("{\"bool\": {\"must\": [{\"terms\": {\"categories\": ?0}}, {\"term\": {\"sold\": false}}]}}}")
    List<ProductDocument> findByCategoriesIn(@Param("userCategories") Set<String> userCategories);

    @Query("{\"bool\": {\"must\": [{\"term\": {\"sold\": false}}]}}")
    List<ProductDocument> findTop20ByOrderByLikeCountDesc();

    @Query("{\"bool\": {\"must\": [{\"term\": {\"sold\": false}}]}}")
    List<ProductDocument> findTop20ByOrderByCreatedAtDesc();

    //포인트가 높은 사람들의 물건 조회하기
    Iterable<ProductDocument> findByMemberIdIn(List<Long> memberIds);

}
