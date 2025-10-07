# Chat Hỗ Trợ Khách Hàng - WebSocket & Spring Boot

## Tổng Quan

Ứng dụng chat real-time được xây dựng bằng Spring Boot và WebSocket, hỗ trợ giao tiếp giữa khách hàng và nhân viên hỗ trợ.

## Công Nghệ Sử Dụng

- **Backend**: Spring Boot 3.5.6, Java 21
- **WebSocket**: Spring WebSocket với STOMP protocol
- **Frontend**: HTML5, CSS3, JavaScript, Bootstrap 5
- **Template Engine**: Thymeleaf
- **Build Tool**: Maven

## Cơ Chế Hoạt Động WebSocket

### 1. Handshake Process
```
Client → Server: HTTP Request với header "Upgrade: websocket"
Server → Client: HTTP 101 Switching Protocols
Connection: Chuyển đổi từ HTTP sang WebSocket
```

### 2. Message Flow
```
Client → Server: STOMP message qua WebSocket
Server → Client: Broadcast message đến tất cả subscribers
```

### 3. STOMP Protocol
- **Destination**: `/app/chat.sendMessage`, `/app/chat.addUser`
- **Subscription**: `/topic/public`
- **Message Types**: CHAT, JOIN, LEAVE, TYPING, STOP_TYPING

## Cài Đặt và Chạy Ứng Dụng

### Yêu Cầu Hệ Thống
- Java 21+
- Maven 3.6+

### Các Bước Cài Đặt

1. **Clone repository**
```bash
git clone <repository-url>
cd BT10_1007
```

2. **Build ứng dụng**
```bash
mvn clean install
```

3. **Chạy ứng dụng**
```bash
mvn spring-boot:run
```

4. **Truy cập ứng dụng**
- Mở trình duyệt và truy cập: `http://localhost:8080`
- Chọn vai trò (Khách hàng hoặc Nhân viên hỗ trợ)
- Nhập tên và bắt đầu chat

## Cấu Trúc Dự Án

```
src/
├── main/
│   ├── java/vn/
│   │   ├── Bt101007Application.java          # Main application class
│   │   ├── config/
│   │   │   └── WebSocketConfig.java         # WebSocket configuration
│   │   ├── controller/
│   │   │   └── ChatController.java          # Chat controller
│   │   └── model/
│   │       ├── ChatMessage.java             # Message model
│   │       └── User.java                    # User model
│   └── resources/
│       ├── application.properties           # Application configuration
│       └── templates/
│           ├── index.html                   # Home page
│           └── chat.html                    # Chat interface
```

## Tính Năng Chính

### 1. Real-time Messaging
- Gửi và nhận tin nhắn real-time
- Hiển thị thời gian gửi tin nhắn
- Phân biệt tin nhắn của mình và người khác

### 2. User Management
- Hỗ trợ nhiều người dùng cùng lúc
- Phân biệt vai trò: Khách hàng và Nhân viên hỗ trợ
- Hiển thị danh sách người dùng online

### 3. Typing Indicators
- Hiển thị khi ai đó đang gõ tin nhắn
- Tự động ẩn sau 3 giây

### 4. Join/Leave Notifications
- Thông báo khi có người tham gia hoặc rời khỏi chat
- Cập nhật danh sách người dùng online

## API Endpoints

### WebSocket Endpoints
- **Connection**: `/ws`
- **Send Message**: `/app/chat.sendMessage`
- **Add User**: `/app/chat.addUser`
- **Typing**: `/app/chat.typing`
- **Stop Typing**: `/app/chat.stopTyping`

### HTTP Endpoints
- **Home Page**: `GET /`
- **Chat Page**: `GET /chat?role={role}&username={username}`

## Cấu Hình

### application.properties
```properties
# WebSocket Configuration
spring.websocket.sockjs.enabled=true
spring.websocket.sockjs.path=/ws

# Server Configuration
server.port=8080

# Logging
logging.level.vn=DEBUG
logging.level.org.springframework.web.socket=DEBUG
```

## Sử Dụng

### 1. Truy Cập Trang Chủ
- Mở `http://localhost:8080`
- Nhập tên và chọn vai trò
- Click "Tham Gia Chat"

### 2. Chat Interface
- Gõ tin nhắn vào ô input
- Nhấn Enter hoặc click nút gửi
- Xem tin nhắn real-time
- Theo dõi danh sách người dùng online

### 3. Tính Năng Nâng Cao
- **Typing Indicator**: Hiển thị khi ai đó đang gõ
- **Message History**: Lưu trữ lịch sử tin nhắn
- **User Status**: Hiển thị trạng thái online/offline

## Mở Rộng

### Thêm Tính Năng
1. **File Upload**: Gửi file đính kèm
2. **Emoji Support**: Hỗ trợ emoji
3. **Private Messages**: Tin nhắn riêng tư
4. **Message Encryption**: Mã hóa tin nhắn
5. **Database Integration**: Lưu trữ tin nhắn vào database

### Cải Thiện Performance
1. **Message Queuing**: Sử dụng Redis hoặc RabbitMQ
2. **Load Balancing**: Phân tải cho nhiều server
3. **Caching**: Cache tin nhắn gần đây
4. **Compression**: Nén tin nhắn

## Troubleshooting

### Lỗi Thường Gặp
1. **WebSocket Connection Failed**: Kiểm tra firewall và proxy
2. **Messages Not Received**: Kiểm tra STOMP subscription
3. **Typing Indicator Not Working**: Kiểm tra JavaScript console

### Debug
- Bật debug logging trong `application.properties`
- Kiểm tra browser console để xem lỗi JavaScript
- Sử dụng browser developer tools để debug WebSocket

## Tác Giả
Lê Văn Chiến Thắng 23110328

## License

MIT License
