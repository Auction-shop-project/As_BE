package Auction_shop.auction.domain.like.service;

import Auction_shop.auction.domain.like.Like;
import Auction_shop.auction.domain.like.repository.LikeRepository;
import Auction_shop.auction.domain.member.Member;
import Auction_shop.auction.domain.member.service.MemberService;
import Auction_shop.auction.domain.product.Product;
import Auction_shop.auction.domain.product.ProductDocument;
import Auction_shop.auction.domain.product.repository.ProductElasticsearchRepository;
import Auction_shop.auction.domain.product.repository.ProductJpaRepository;
import Auction_shop.auction.web.dto.product.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final ProductJpaRepository productRepository;
    private final ProductElasticsearchRepository productElasticsearchRepository;
    private final MemberService memberService;
    private final ProductMapper productMapper;

    @Override
    public Like addProductToLike(Long memberId, Long productId) {
        Member member = memberService.getById(memberId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException(productId+"에 해당하는 물품이 없습니다."));

        if (likeRepository.findByMemberAndProduct(member, product) != null){
            throw new RuntimeException("이미 좋아요 되어있습니다.");
        }

        Like like = Like.builder()
                .member(member)
                .product(product)
                .build();

        member.addLike(like);
        product.addLike(like);

        ProductDocument productDocument = productMapper.toDocument(product);
        productElasticsearchRepository.save(productDocument);

        return likeRepository.save(like);
    }

    @Override
    public List<Like> getLikeList(Long memberId) {
        Member member = memberService.getById(memberId);
        return likeRepository.findByMember(member);
    }

    @Override
    public List<Long> getLikeItems(Long memberId){
        Member member = memberService.getById(memberId);
        List<Like> likes = likeRepository.findByMember(member);
        return likes.stream()
                .map(like -> like.getProduct().getId()) // 제품 ID 추출
                .collect(Collectors.toList());
    }

    @Override
    public void removeProductFromLike(Long memberId, Long productId) {
        Member member = memberService.getById(memberId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException(productId+"에 해당하는 물품이 없습니다."));

        Like like = likeRepository.findByMemberAndProduct(member, product);
        if(like != null){
            member.removeLike(like);
            product.removeLike(like);
            ProductDocument productDocument = productMapper.toDocument(product);
            productElasticsearchRepository.save(productDocument);
            likeRepository.delete(like);
        }
    }

    @Override
    public boolean isLiked(Long memberId, Long productId) {
        Member member = memberService.getById(memberId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException(productId+"에 해당하는 물품이 없습니다."));
        Like like = likeRepository.findByMemberAndProduct(member, product);
        if(like != null){
            return true;
        }
        return false;
    }

    public int getProductLikeCount(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException(productId+"에 해당하는 물품이 없습니다."));

        return product.getLikeCount();
    }
}
