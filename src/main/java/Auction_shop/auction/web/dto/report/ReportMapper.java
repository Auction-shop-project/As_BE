package Auction_shop.auction.web.dto.report;

import Auction_shop.auction.domain.report.Report;

import java.util.List;

public interface ReportMapper {

    ReportResponseDto toResponseDto(Report report);
    ReportListResponseDto toListResponseDto(List<Report> reports);
}
