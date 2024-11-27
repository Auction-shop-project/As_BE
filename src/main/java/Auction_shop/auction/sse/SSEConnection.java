package Auction_shop.auction.sse;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@AllArgsConstructor
@RestController
public class SSEConnection {
    @Qualifier("longRedisTemplate")
    private final RedisTemplate<String, Long> messageQueue;
    private static final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final long TIMEOUT = 5 * 60 * 1000L; // 5분
    private final long RECONNECTION_TIMEOUT = 5000L; // 5초

    @GetMapping(value = "/sse/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connectSSEConnect(@RequestParam Long userId) {
        SseEmitter emitter = new SseEmitter(TIMEOUT);
        emitters.put(userId, emitter);

        String idTypeChange = String.valueOf(userId);
        List<Long> chatRoomIds = messageQueue.opsForList().range(idTypeChange, 0, -1);
        sendEvent(emitter, "connect", chatRoomIds);
        messageQueue.delete(idTypeChange);

        emitter.onTimeout(() -> {
            emitter.complete();
            emitters.remove(userId); // 해당 유저의 emitter 제거
        });

        emitter.onCompletion(() -> {
            log.info("onCompletion callback run");
            emitters.remove(userId); // 해당 유저의 emitter 제거
        });

        return ResponseEntity.ok(emitter);
    }

    public void sendEvent(SseEmitter emitter, String name, Object roomId) {
        try {
            emitter.send(SseEmitter.event()
                    .name(name)
                    .data(roomId)
                    .reconnectTime(RECONNECTION_TIMEOUT)
            );
        } catch (IOException e) {
            log.info("SSE Connection Error = {}", e.getMessage());
            emitters.remove(emitter); // 오류 발생 시 emitter 제거
        }
    }

    public SseEmitter getEmitter(Long yourId) {
        return emitters.get(yourId);
    }
}