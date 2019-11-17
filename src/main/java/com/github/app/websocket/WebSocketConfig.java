package com.github.app.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.server.standard.TomcatRequestUpgradeStrategy;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketConfig.class);
    private final TextWebSocketHandler voteCountWebSocketHandler;

    public WebSocketConfig(TextWebSocketHandler voteCountWebSocketHandler) {
        this.voteCountWebSocketHandler = voteCountWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        LOGGER.info("Registering web socket handler");
        webSocketHandlerRegistry.addHandler(voteCountWebSocketHandler, "/vote-count")
                .setAllowedOrigins("*")
                .setHandshakeHandler(new DefaultHandshakeHandler(new TomcatRequestUpgradeStrategy()));
    }
}
