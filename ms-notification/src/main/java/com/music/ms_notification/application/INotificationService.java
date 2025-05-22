package com.music.ms_notification.application;

public interface INotificationService {

    // Send an email with a verification code
    void sendOtpEmail(String email, Integer otp);
}
