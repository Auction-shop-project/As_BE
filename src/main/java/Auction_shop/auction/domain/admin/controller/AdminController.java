package Auction_shop.auction.domain.admin.controller;

import Auction_shop.auction.domain.inquriy.Inquiry;
import Auction_shop.auction.domain.inquriy.repository.InquiryRepository;
import Auction_shop.auction.domain.inquriy.service.InquiryService;
import Auction_shop.auction.domain.member.Member;
import Auction_shop.auction.domain.member.repository.MemberRepository;
import Auction_shop.auction.domain.member.service.MemberService;
import Auction_shop.auction.domain.notice.Notice;
import Auction_shop.auction.domain.notice.repository.NoticeRepository;
import Auction_shop.auction.domain.notice.service.NoticeService;
import Auction_shop.auction.domain.product.service.ProductService;
import Auction_shop.auction.domain.report.Report;
import Auction_shop.auction.domain.report.repository.ReportRepository;
import Auction_shop.auction.web.dto.admin.CreateAnswerDto;
import Auction_shop.auction.web.dto.inquiry.InquiryListResponseDto;
import Auction_shop.auction.web.dto.inquiry.InquiryMapper;
import Auction_shop.auction.web.dto.inquiry.InquiryResponseDto;
import Auction_shop.auction.web.dto.member.MemberListResponseDto;
import Auction_shop.auction.web.dto.member.MemberMapper;
import Auction_shop.auction.web.dto.notice.NoticeCreateDto;
import Auction_shop.auction.web.dto.notice.NoticeMapper;
import Auction_shop.auction.web.dto.notice.NoticeResponseDto;
import Auction_shop.auction.web.dto.notice.NoticeUpdateDto;
import Auction_shop.auction.web.dto.report.ReportListResponseDto;
import Auction_shop.auction.web.dto.report.ReportMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class AdminController {
    private final MemberService memberService;
    private final ProductService productService;
    private final InquiryService inquiryService;
    private final NoticeService noticeService;
    private final MemberRepository memberRepository;
    private final InquiryRepository inquiryRepository;
    private final NoticeRepository noticeRepository;
    private final ReportRepository reportRepository;
    private final MemberMapper memberMapper;
    private final InquiryMapper inquiryMapper;
    private final NoticeMapper noticeMapper;
    private final ReportMapper reportMapper;

    //회원 관련

    //전체 회원 조회
    @GetMapping("/members")
    public ResponseEntity<List<MemberListResponseDto>> getAllMember(){
        List<Member> members = memberRepository.findAll();
        List<MemberListResponseDto> collect = members.stream()
                .map(memberMapper::toListResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(collect);
    }

    //회원 삭제
    @DeleteMapping("/member/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable("id") Long id){
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    //회원 권한 변경 (USER, ADMIN)
    @PutMapping("/{id}/role")
    public ResponseEntity<Void> updateRole(@PathVariable("id") Long id, @RequestBody String role){
        Member member = memberRepository.findById(id).orElse(null);
        member.updateRole(role);
        return ResponseEntity.noContent().build();
    }

    //문의 관련

    //문의 조회
    @GetMapping("/inquiries/{status}")
    public ResponseEntity<List<InquiryListResponseDto>> getInquiries(@PathVariable("status") boolean status){
        List<Inquiry> inquiries = inquiryRepository.findByStatus(status);
        List<InquiryListResponseDto> collect = inquiries.stream()
                .map(inquiryMapper::toListResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(collect);
    }

    //문의 답변
    @PostMapping("/inquiry/{id}")
    public ResponseEntity<InquiryResponseDto> addAnswer(@PathVariable("id") Long id, @RequestBody CreateAnswerDto createAnswerDto){
        Inquiry inquiry = inquiryService.addAnswer(id, createAnswerDto.getContent());
        InquiryResponseDto collect = inquiryMapper.toResponseDto(inquiry);
        return ResponseEntity.status(HttpStatus.CREATED).body(collect);
    }

    //문의 삭제
    @DeleteMapping("/inquiry/{id}")
    public ResponseEntity<Void> deleteInquiry(@PathVariable("id") Long id){
        inquiryRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    //경매 관련

    //경매 삭제
    @DeleteMapping("/product/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id){
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }

    //공지 관련

    //공지 등록
    @PostMapping("/notice")
    public ResponseEntity<NoticeResponseDto> createNotice(@RequestBody NoticeCreateDto noticeCreateDto){
        Notice notice = noticeService.createNotice(noticeCreateDto);
        NoticeResponseDto collect = noticeMapper.toResponseDto(notice);
        return ResponseEntity.status(HttpStatus.CREATED).body(collect);
    }

    //공지 수정
    @PutMapping("/notice/{id}")
    public ResponseEntity<NoticeResponseDto> updateNotice(@PathVariable("id") Long id, @RequestBody NoticeUpdateDto noticeUpdateDto){
        Notice notice = noticeService.updateNotice(id, noticeUpdateDto);
        NoticeResponseDto collect = noticeMapper.toResponseDto(notice);
        return ResponseEntity.ok(collect);
    }

    //공지 삭제
    @DeleteMapping("/notice/{id}")
    public ResponseEntity<Void> deleteNotice(@PathVariable("id") Long id){
        noticeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    //신고 관련

    //신고 전체 조회
    @GetMapping("/reports")
    public ResponseEntity<ReportListResponseDto> getAllReport(){
        List<Report> reports = reportRepository.findAll();
        ReportListResponseDto collect = reportMapper.toListResponseDto(reports);
        return ResponseEntity.ok(collect);
    }
}
