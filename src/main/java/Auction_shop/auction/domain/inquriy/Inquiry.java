package Auction_shop.auction.domain.inquriy;

import Auction_shop.auction.util.BaseEntity;
import Auction_shop.auction.domain.image.Image;
import Auction_shop.auction.domain.member.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inquiry extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inquiry_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 1000)
    private String content;

    private String answer;

    private boolean status;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "inquiry_id")
    @Builder.Default
    private List<Image> imageList = new ArrayList<>();

    public void setImageList(List<Image> imageList){
        this.imageList = imageList;
    }

    public void setMember(Member member){
        this.member = member;
    }

    //비즈니스 로직
    public List<String> getImageUrls(){
        return imageList.stream()
                .map(Image::getAccessUrl)
                .collect(Collectors.toList());
    }

    public void updateInquiry(String title, String content){
        this.title = title;
        this.content = content;
    }

    public void addAnswer(String answer){
        this.answer = answer;
        this.status = true;
    }
}
