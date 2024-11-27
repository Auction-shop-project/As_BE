package Auction_shop.auction.domain.alert.util;

import Auction_shop.auction.domain.alert.AlertType;
import Auction_shop.auction.domain.alert.service.AlertService;
import Auction_shop.auction.sse.SSEConnection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
@RequiredArgsConstructor
public class AlertUtil {
    private final AlertService alertService;
    private final SSEConnection sseConnection;

    public void run(Long memberId, String memberNickname, String content, AlertType alertType){
        SseEmitter emitter = sseConnection.getEmitter(memberId);
        if (emitter != null) {
            sseConnection.sendEvent(emitter, content, null);
        }
        alertService.add(memberId, memberNickname, "AddLike", alertType);
    }
}
