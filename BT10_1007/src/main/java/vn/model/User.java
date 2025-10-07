package vn.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String username;
    private String sessionId;
    private boolean isOnline;
    private String role; // "CUSTOMER" hoặc "SUPPORT"
}
