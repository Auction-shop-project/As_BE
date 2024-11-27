package Auction_shop.auction.domain.report.service;

import Auction_shop.auction.domain.member.Member;
import Auction_shop.auction.domain.member.repository.MemberRepository;
import Auction_shop.auction.domain.report.Report;
import Auction_shop.auction.domain.report.repository.ReportRepository;
import Auction_shop.auction.web.dto.report.ReportCreateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;

    public List<Report> getReports(Long id) {
        return reportRepository.findByReporterId(id);
    }

    @Transactional
    public Report create(Long memberId, ReportCreateDto createDto) {
        Member reporter = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException(memberId + "에 해당하는 회원이 없습니다."));

        Member reported = memberRepository.findById(createDto.getReportedId())
                .orElseThrow(() -> new IllegalArgumentException(memberId + "에 해당하는 회원이 없습니다."));

        Report report = Report.builder()
                .reporter(reporter)
                .reported(reported)
                .content(createDto.getContent())
                .build();

        reportRepository.save(report);

        return report;
    }
}