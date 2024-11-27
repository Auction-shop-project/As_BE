package Auction_shop.auction.chat.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/sub");
        config.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        /// FIXME: 6/24/24 테스트 위해 .setAllowedOrigins("*"), 이후에 허용 범위 수정
        // NOTE: localhost(Ipv4):8080/ws 경로로 웹소켓 연결 요청 ex) ws://localhost:8080/ws
        registry.addEndpoint("/ws").setAllowedOrigins("*");
    }
}
