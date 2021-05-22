package com.drivers.suggestion.kafka.consumer;

import com.drivers.suggestion.model.Driver;
import com.drivers.suggestion.service.IDriverService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Kafka consumer
 */
@Component
public class Consumer {

    @Autowired
    IDriverService baseService;

    /**
     * Triggers when a message is published in the topic by the producer. takes the list of data and inserts into database.
     * @param payload - list of data
     */
    @KafkaListener(topics = "${spring.kafka.common.topic}", groupId = "${spring.kafka.consumer.group-id}"/*, containerFactory = "kafkaListenerContainerFactory"*/)
    public void listenDriversData(@Payload String payload) {
        ObjectMapper mapper = new ObjectMapper();
        List<Driver> deserializedPayload;
        try {
            deserializedPayload = mapper.readValue(payload, new TypeReference<List<Driver>>() {});
            baseService.insertDriverDetails(deserializedPayload);
        } catch (JsonProcessingException e) {
            System.out.println("Unhandled JSON Exception! further message - " + e.getMessage());
        }
    }

}
