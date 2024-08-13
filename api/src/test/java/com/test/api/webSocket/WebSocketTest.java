package com.test.api.webSocket;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.Collections;

@SpringBootTest
public class WebSocketTest {

    @Autowired
    private WebSocketClient webSocketClient;

    @Test
    public void testWebSocketConnection() {
        WebSocketClient mockClient = Mockito.mock(WebSocketClient.class);
        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(Collections.singletonList(new WebSocketTransport(mockClient))));
        StompSessionHandler sessionHandler = new StompSessionHandlerAdapter() {
            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                System.out.println("Connected!");
                session.subscribe("/topic/users", new MessageHandler());
                session.send("/app/someEndpoint", "{\"key\": \"value\"}");
            }
        };

        stompClient.connect("ws://localhost:8080/ws", sessionHandler);
    }

    private static class MessageHandler implements StompFrameHandler {
        @Override
        public Type getPayloadType(StompHeaders headers) {
            return byte[].class;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            System.out.println("Received message: " + new String((byte[]) payload));
        }
    }
}
