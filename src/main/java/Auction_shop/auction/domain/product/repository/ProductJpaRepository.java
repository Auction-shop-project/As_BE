package Auction_shop.auction.domain.product.repository;

import Auction_shop.auction.domain.product.Product;
import Auction_shop.auction.domain.product.ProductType;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProductJpaRepository extends JpaRepository<Product,Long> {

    //종료 시간이 지나지 않은 물건 중 경매가 종료되지 않은 물건만 서치
    @Query("SELECT p FROM Product p WHERE p.endTime > :currentTime AND p.isSold = false AND p.productType = :productType")
    Page<Product> findActiveProduct(@Param("currentTime") LocalDateTime currentTime, @Param("productType") ProductType productType, Pageable pageable);

    //종료 시간이 지난 물건중 경매가 끝난 물건만 서치
    @Query("SELECT p FROM Product p WHERE p.endTime <= :currentTime AND p.productType = :productType AND p.isSold = false")
    Page<Product> findExpiredProduct(@Param("currentTime") LocalDateTime currentTime, @Param("productType") ProductType productType, Pageable pageable);

    @Query("SELECT p.current_price FROM Product p WHERE p.id = :productId")
    int findCurrentPriceById(@Param("productId") Long productId);

    @Query("SELECT p.productType FROM Product p WHERE p.id = :productId")
    ProductType findProductTypeById(@Param("productId") Long productId);

    List<Product> findAllByMemberId(Long memberId);

    @Query("SELECT COUNT(p) > 0 FROM Product p WHERE p.member.id = :memberId AND p.isSold = false")
    boolean existsActiveProductsByMemberId(@Param("memberId") Long memberId);

    //동시성 방지를 위해 Lock 을 건 물건 반환
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.id = :productId")
    Optional<Product> findByProductIdWithLock(@Param("productId") Long productId);
}
