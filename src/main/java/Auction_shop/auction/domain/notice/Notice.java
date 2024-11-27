package Auction_shop.auction.domain.notice;

import Auction_shop.auction.util.BaseEntity;
import Auction_shop.auction.web.dto.notice.NoticeUpdateDto;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notice extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long id;
    private String title;
    private String content;

    public void update(NoticeUpdateDto noticeUpdateDto){
        if(StringUtils.isNotBlank(noticeUpdateDto.getTitle())){
            this.title = noticeUpdateDto.getTitle();
        }

        if(StringUtils.isNotBlank(noticeUpdateDto.getContent())){
            this.content = noticeUpdateDto.getContent();
        }
    }
}
