# Hướng Dẫn Chi Tiết về WebSocket

## 1. WebSocket là gì?

WebSocket là một giao thức truyền thông hai chiều (bidirectional) cho phép client và server trao đổi dữ liệu real-time thông qua một kết nối TCP duy nhất. Khác với HTTP request-response truyền thống, WebSocket duy trì kết nối mở và cho phép cả hai phía gửi dữ liệu bất cứ lúc nào.

## 2. So Sánh WebSocket vs HTTP

| Đặc điểm | HTTP | WebSocket |
|----------|------|-----------|
| **Kết nối** | Tạm thời (per request) | Dài hạn (persistent) |
| **Hướng** | Một chiều (client → server) | Hai chiều (client ↔ server) |
| **Overhead** | Cao (headers mỗi request) | Thấp (sau handshake) |
| **Real-time** | Không (polling required) | Có (push messages) |
| **Latency** | Cao | Thấp |

## 3. Cơ Chế Hoạt Động

### 3.1 Handshake Process
```
1. Client gửi HTTP request với header:
   - Upgrade: websocket
   - Connection: Upgrade
   - Sec-WebSocket-Key: [random key]
   - Sec-WebSocket-Version: 13

2. Server phản hồi với:
   - HTTP 101 Switching Protocols
   - Upgrade: websocket
   - Connection: Upgrade
   - Sec-WebSocket-Accept: [calculated key]

3. Kết nối được nâng cấp thành WebSocket
```

### 3.2 Message Types
- **Text Frame**: Dữ liệu text
- **Binary Frame**: Dữ liệu binary
- **Close Frame**: Đóng kết nối
- **Ping/Pong Frame**: Keep-alive

## 4. STOMP Protocol

STOMP (Simple Text Oriented Messaging Protocol) là một giao thức messaging đơn giản chạy trên WebSocket.

### 4.1 STOMP Commands
- **CONNECT**: Kết nối đến server
- **SEND**: Gửi message
- **SUBSCRIBE**: Đăng ký nhận message
- **UNSUBSCRIBE**: Hủy đăng ký
- **DISCONNECT**: Ngắt kết nối

### 4.2 STOMP Headers
```
CONNECT
accept-version:1.1,1.0
host:localhost
login:guest
passcode:guest

SEND
destination:/app/chat.sendMessage
content-type:application/json

SUBSCRIBE
id:sub-0
destination:/topic/public
```

## 5. Spring WebSocket Implementation

### 5.1 Configuration
```java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Enable simple broker for destinations starting with "/topic"
        config.enableSimpleBroker("/topic", "/queue");
        // Set prefix for messages from client
        config.setApplicationDestinationPrefixes("/app");
    }
    
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Register WebSocket endpoint
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS(); // Enable SockJS fallback
    }
}
```

### 5.2 Message Handling
```java
@Controller
public class ChatController {
    
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }
    
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                              SimpMessageHeaderAccessor headerAccessor) {
        // Add user to session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }
}
```

## 6. Frontend Implementation

### 6.1 JavaScript WebSocket Client
```javascript
// Kết nối WebSocket
const socket = new SockJS('/ws');
const stompClient = Stomp.over(socket);

stompClient.connect({}, function (frame) {
    console.log('Connected: ' + frame);
    
    // Subscribe to messages
    stompClient.subscribe('/topic/public', function (message) {
        const chatMessage = JSON.parse(message.body);
        displayMessage(chatMessage);
    });
});

// Gửi message
function sendMessage() {
    const chatMessage = {
        sender: username,
        content: messageInput.value,
        type: 'CHAT'
    };
    
    stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
}
```

### 6.2 SockJS Fallback
SockJS cung cấp fallback cho các trình duyệt không hỗ trợ WebSocket:
- WebSocket (native)
- WebSocket over HTTP (streaming)
- HTTP long polling
- HTTP polling

## 7. Best Practices

### 7.1 Security
- Validate tất cả input
- Implement authentication/authorization
- Use HTTPS/WSS in production
- Rate limiting cho messages

### 7.2 Performance
- Implement message queuing (Redis, RabbitMQ)
- Use connection pooling
- Monitor memory usage
- Implement heartbeat/ping-pong

### 7.3 Error Handling
```javascript
stompClient.connect({}, function (frame) {
    // Success
}, function (error) {
    // Error handling
    console.log('STOMP error: ' + error);
});
```

## 8. Monitoring và Debug

### 8.1 Logging
```properties
# application.properties
logging.level.org.springframework.web.socket=DEBUG
logging.level.org.springframework.messaging=DEBUG
```

### 8.2 Browser DevTools
- Network tab: Xem WebSocket connections
- Console: Debug JavaScript errors
- Application tab: Xem WebSocket frames

## 9. Production Considerations

### 9.1 Load Balancing
- Sticky sessions required
- Use Redis for session sharing
- Implement message broadcasting across servers

### 9.2 Scaling
- Horizontal scaling với message broker
- Database cho message persistence
- CDN cho static resources

### 9.3 Monitoring
- Connection count monitoring
- Message throughput metrics
- Error rate tracking
- Performance monitoring

## 10. Troubleshooting

### 10.1 Common Issues
1. **Connection refused**: Check server status, firewall
2. **Messages not received**: Verify subscription paths
3. **CORS errors**: Configure allowed origins
4. **Memory leaks**: Proper cleanup of subscriptions

### 10.2 Debug Tools
- Browser WebSocket inspector
- Network monitoring tools
- Application logs
- Performance profilers

## 11. Tài Liệu Tham Khảo

- [WebSocket RFC 6455](https://tools.ietf.org/html/rfc6455)
- [STOMP Protocol](https://stomp.github.io/)
- [Spring WebSocket Documentation](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#websocket)
- [SockJS Documentation](https://github.com/sockjs/sockjs-client)
