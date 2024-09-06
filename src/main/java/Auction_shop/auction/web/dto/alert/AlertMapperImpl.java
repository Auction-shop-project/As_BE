package Auction_shop.auction.web.dto.alert;

import Auction_shop.auction.domain.alert.Alert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AlertMapperImpl implements AlertMapper {

    @Override
    public AlertResponseDto toResponseDto(Alert alert) {
        AlertResponseDto responseDto = new AlertResponseDto().builder()
                .id(alert.getId())
                .memberId(alert.getMemberId())
                .content(alert.getContent()).
                build();
        return responseDto;
    }

    @Override
    public AlertListResponseDto toListResponseDto(List<Alert> alerts) {
        AlertListResponseDto toListResponseDto = new AlertListResponseDto().builder()
                .list(alerts.stream().map(a -> toResponseDto(a)).toList())
                .build();
        return toListResponseDto;
    }
}
