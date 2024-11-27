package Auction_shop.auction.domain.image;

import Auction_shop.auction.util.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Image {

    @Id
    @JoinColumn(name = "image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originName;

    private String storedName;

    private String accessUrl;

    public Image(String originName){
        this.originName = originName;
        this.storedName = getFileName(originName);
        this.accessUrl = "";
    }

    //AccessUrl setter
    public void setAccessUrl(String accessUrl){
        this.accessUrl = accessUrl;
    }

    //이미지 파일 확장자 추출 메소드
    public String extractExtension(String originName){
        int index = originName.lastIndexOf('.');
        return originName.substring(index, originName.length());
    }

    //이미지 파일 이름 변환 메소드
    public String getFileName(String originName){
        return UUID.randomUUID() + extractExtension(originName);
    }
}
