package com.drivers.suggestion.kafka.consumer;

import com.drivers.suggestion.model.Driver;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Consumer {

    @KafkaListener(topics = "${spring.kafka.common.topic}", groupId = "${spring.kafka.consumer.groupID}", containerFactory = "kafkaListenerContainerFactory")
    public void listenDriversData(@Payload String payload) {
        System.out.println("Message received - ");
        ObjectMapper mapper = new ObjectMapper();
        List<Driver> deserializedPayload;
        try {
            deserializedPayload = mapper.readValue(payload, new TypeReference<List<Driver>>() {});
            for(Driver driver: deserializedPayload)
                System.out.println(driver);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
