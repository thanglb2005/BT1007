package vn.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    private String content;
    private String sender;
    private String receiver;
    private MessageType type;
    private LocalDateTime timestamp;
    
    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE,
        TYPING,
        STOP_TYPING
    }
}
