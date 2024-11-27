package Auction_shop.auction.domain.notice.controller;

import Auction_shop.auction.domain.notice.Notice;
import Auction_shop.auction.domain.notice.repository.NoticeRepository;
import Auction_shop.auction.web.dto.notice.NoticeListResponseDto;
import Auction_shop.auction.web.dto.notice.NoticeMapper;
import Auction_shop.auction.web.dto.notice.NoticeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeRepository noticeRepository;
    private final NoticeMapper noticeMapper;

    //전체 공지 조회
    @GetMapping("/notices")
    public ResponseEntity<List<NoticeListResponseDto>> getAllNotice(){
        List<Notice> notices = noticeRepository.findAll();
        List<NoticeListResponseDto> collect = notices.stream()
                .map(noticeMapper::toListResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(collect);
    }

    //공지 조회
    @GetMapping("/notices/{id}")
    public ResponseEntity<NoticeResponseDto> getNotice(@PathVariable("id") Long id){
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + "에 해당하는 공지가 없습니다."));
        NoticeResponseDto collect = noticeMapper.toResponseDto(notice);
        return ResponseEntity.ok(collect);
    }
}
