package Auction_shop.auction.domain.notice.service;

import Auction_shop.auction.domain.notice.Notice;
import Auction_shop.auction.domain.notice.repository.NoticeRepository;
import Auction_shop.auction.web.dto.notice.NoticeCreateDto;
import Auction_shop.auction.web.dto.notice.NoticeUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;

    @Transactional
    public Notice createNotice(NoticeCreateDto noticeCreateDto) {
        Notice notice = Notice.builder()
                .title(noticeCreateDto.getTitle())
                .content(noticeCreateDto.getContent())
                .build();

        noticeRepository.save(notice);
        return notice;
    }

    @Transactional
    public Notice updateNotice(Long id, NoticeUpdateDto noticeUpdateDto) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + "에 해당하는 공지가 없습니다."));

        notice.update(noticeUpdateDto);
        return notice;
    }
}
