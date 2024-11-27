package Auction_shop.auction.domain.report.controller;

import Auction_shop.auction.domain.report.Report;
import Auction_shop.auction.domain.report.service.ReportService;
import Auction_shop.auction.security.jwt.JwtUtil;
import Auction_shop.auction.web.dto.report.ReportCreateDto;
import Auction_shop.auction.web.dto.report.ReportListResponseDto;
import Auction_shop.auction.web.dto.report.ReportMapper;
import Auction_shop.auction.web.dto.report.ReportResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;
    private final ReportMapper reportMapper;
    private final JwtUtil jwtUtil;

    @GetMapping("/reports/{id}")
    public ResponseEntity<ReportListResponseDto> getReports(@RequestHeader("Authorization") String authorization,
                                                            @PathVariable("id") Long id){
        Long memberId = jwtUtil.extractMemberId(authorization);
        List<Report> reports = reportService.getReports(id);
        ReportListResponseDto collect = reportMapper.toListResponseDto(reports);
        return ResponseEntity.ok(collect);
    }

    @PostMapping("/report")
    public ResponseEntity<ReportResponseDto> create(@RequestHeader("Authorization") String authorization,
                                                        @RequestBody ReportCreateDto createDto){
        Long memberId = jwtUtil.extractMemberId(authorization);
        Report report = reportService.create(memberId, createDto);
        ReportResponseDto collect = reportMapper.toResponseDto(report);
        return ResponseEntity.ok(collect);
    }
}
