package Auction_shop.auction.web.dto.report;

import Auction_shop.auction.domain.report.Report;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReportMapperImpl implements ReportMapper {

    @Override
    public ReportResponseDto toResponseDto(Report report) {
        return ReportResponseDto.builder()
                .id(report.getId())
                .reporterId(report.getReporter().getId())
                .reportedId(report.getReported().getId())
                .content(report.getContent())
                .build();
    }

    @Override
    public ReportListResponseDto toListResponseDto(List<Report> reports) {
        return ReportListResponseDto.builder()
                .list(reports.stream().map(r -> toResponseDto(r)).toList())
                .build();
    }
}
