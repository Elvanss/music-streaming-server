package com.music.ms_user.event;

import java.time.Instant;

public class OtpRequestEvent {
    private String email;
    private String password;
    private String type = "LOGIN";
    private Instant timestamp;

    public OtpRequestEvent() {}

    public OtpRequestEvent(String email, String password) {
        this.email = email;
        this.password = password;
        this.timestamp = Instant.now();
    }

    // Getters, Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
}

