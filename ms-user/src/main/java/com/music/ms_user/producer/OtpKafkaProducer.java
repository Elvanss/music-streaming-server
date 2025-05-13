package com.music.ms_user.producer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.music.ms_user.event.OtpRequestEvent;

@Service
public class OtpKafkaProducer {

    private final KafkaTemplate<String, OtpRequestEvent> kafkaTemplate;

    @Value("${app.topic.otp-request}")
    private String topic;

    public OtpKafkaProducer(KafkaTemplate<String, OtpRequestEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOtpRequest(OtpRequestEvent event) {
        kafkaTemplate.send(topic, event);
    }
}
