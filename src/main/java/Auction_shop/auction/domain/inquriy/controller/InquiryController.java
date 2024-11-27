package Auction_shop.auction.domain.inquriy.controller;

import Auction_shop.auction.domain.inquriy.Inquiry;
import Auction_shop.auction.domain.inquriy.service.InquiryService;
import Auction_shop.auction.security.jwt.JwtUtil;
import Auction_shop.auction.web.dto.inquiry.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inquiry")
public class InquiryController {

    private final InquiryService inquiryService;
    private final InquiryMapper inquiryMapper;
    private final JwtUtil jwtUtil;

    //등록
    @PostMapping
    public ResponseEntity<InquiryResponseDto> createInquiry(
            @RequestHeader("Authorization") String authorization,
            @RequestPart("inquiry") final InquiryCreateDto inquiryDto,
            @RequestPart(value = "images", required = false) final List<MultipartFile> images){
        Long memberId = jwtUtil.extractMemberId(authorization);
        Inquiry inquiry = inquiryService.createInquiry(inquiryDto, memberId, images);
        InquiryResponseDto collect = inquiryMapper.toResponseDto(inquiry);
        return ResponseEntity.status(HttpStatus.CREATED).body(collect);
    }

    //멤버 문의 조회
    @GetMapping("/member")
    public ResponseEntity<List<InquiryListResponseDto>> getAllByMemberId(@RequestHeader("Authorization") String authorization){
        Long memberId = jwtUtil.extractMemberId(authorization);
        List<Inquiry> inquiries = inquiryService.getAllByMemberId(memberId);
        if (inquiries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<InquiryListResponseDto> collect = inquiries.stream()
                .map(inquiryMapper::toListResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(collect);
    }

    //개별 조회
    @GetMapping("/{inquiryId}")
    public ResponseEntity<InquiryResponseDto> getByInquiryId(@PathVariable Long inquiryId){
        Inquiry inquiry = inquiryService.getById(inquiryId);
        InquiryResponseDto collect = inquiryMapper.toResponseDto(inquiry);

        return ResponseEntity.ok(collect);
    }

    //수정
    @PutMapping("/{inquiryId}")
    public ResponseEntity<InquiryResponseDto> updateInquiry(
            @PathVariable Long inquiryId, @RequestPart(value = "inquiry") InquiryUpdateDto inquiryDto,
            @RequestPart(value = "images", required = false) final List<MultipartFile> images){
        Inquiry inquiry = inquiryService.updateInquiry(inquiryId, inquiryDto, images);
        InquiryResponseDto collect = inquiryMapper.toResponseDto(inquiry);

        return ResponseEntity.ok(collect);
    }

    //삭제
    @DeleteMapping("/{inquiryId}")
    public ResponseEntity<Object> deleteInquiry(@PathVariable Long inquiryId){
        boolean isFound = inquiryService.deleteInquiry(inquiryId);
        if (isFound) {
            return ResponseEntity.status(HttpStatus.OK).body("delete success");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Delete failed. check product_id and Database");
        }
    }
}
