package com.drivers.suggestion.kafka.consumer;

import com.drivers.suggestion.controller.IBackfeedController;
import com.drivers.suggestion.model.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.List;

public class Consumer {

    @Autowired
    IBackfeedController backfeedController;

    @KafkaListener(topics = "${spring.kafka.consumer.topic}", groupId = "${spring.kafka.consumer.groupID}", containerFactory = "kafkaListenerContainerFactory")
    public void listenDriversData(@Payload List<Driver> payload) {
        backfeedController.postDriversData(payload);
    }

}
