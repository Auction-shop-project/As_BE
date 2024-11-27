package Auction_shop.auction.domain.report.repository;

import Auction_shop.auction.domain.report.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    @Query("select r from Report r join fetch r.reporter join fetch r.reported where r.reporter.id = :id")
    List<Report> findByReporterId(@Param("id") Long id);
}
