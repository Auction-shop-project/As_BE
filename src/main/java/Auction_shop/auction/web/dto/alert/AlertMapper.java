package Auction_shop.auction.web.dto.alert;

import Auction_shop.auction.domain.alert.Alert;
import java.util.List;

public interface AlertMapper {
    AlertResponseDto toResponseDto(Alert alert);
    AlertListResponseDto toListResponseDto(List<Alert> alerts);
}
