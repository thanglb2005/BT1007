package vn.controller;

import vn.model.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ChatController {

    private final List<ChatMessage> chatHistory = new ArrayList<>();

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/chat")
    public String chat(@RequestParam(defaultValue = "CUSTOMER") String role, 
                      @RequestParam(defaultValue = "Guest") String username, 
                      Model model) {
        model.addAttribute("role", role);
        model.addAttribute("username", username);
        model.addAttribute("chatHistory", chatHistory);
        return "chat";
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        chatMessage.setTimestamp(LocalDateTime.now());
        chatHistory.add(chatMessage);
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, 
                              SimpMessageHeaderAccessor headerAccessor) {
        // Thêm username vào WebSocket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        
        // Thông báo user đã join
        chatMessage.setType(ChatMessage.MessageType.JOIN);
        chatMessage.setContent(chatMessage.getSender() + " đã tham gia chat!");
        chatMessage.setTimestamp(LocalDateTime.now());
        
        return chatMessage;
    }

    @MessageMapping("/chat.typing")
    @SendTo("/topic/public")
    public ChatMessage typing(@Payload ChatMessage chatMessage) {
        chatMessage.setType(ChatMessage.MessageType.TYPING);
        return chatMessage;
    }

    @MessageMapping("/chat.stopTyping")
    @SendTo("/topic/public")
    public ChatMessage stopTyping(@Payload ChatMessage chatMessage) {
        chatMessage.setType(ChatMessage.MessageType.STOP_TYPING);
        return chatMessage;
    }
}
