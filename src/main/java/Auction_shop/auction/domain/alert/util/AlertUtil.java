package Auction_shop.auction.domain.alert.util;

import Auction_shop.auction.sse.SSEConnection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
@RequiredArgsConstructor
public class AlertUtil {
    private final SSEConnection sseConnection;

    public void run(Long memberId, String content){
        SseEmitter emitter = sseConnection.getEmitter(Long.toString(memberId));
        if (emitter != null) {
            sseConnection.sendEvent(emitter, content, null);
        }
    }
}
