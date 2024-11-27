package Auction_shop.auction.web.dto.notice;

import Auction_shop.auction.domain.notice.Notice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NoticeMapperImpl implements NoticeMapper {
    @Override
    public NoticeResponseDto toResponseDto(Notice notice) {
        return NoticeResponseDto.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .date(notice.getUpdatedAt())
                .build();
    }

    @Override
    public NoticeListResponseDto toListResponseDto(Notice notice) {
        return NoticeListResponseDto.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .date(notice.getUpdatedAt())
                .build();
    }
}
