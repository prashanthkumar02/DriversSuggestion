package com.drivers.suggestion.kafka.producer;

import com.drivers.suggestion.model.Driver;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Kafka producer
 */
@Service
public class Producer {

    @Value("${spring.kafka.common.topic}")
    String topicName;

    @Autowired
    private KafkaTemplate<String, String> producer;

    /**
     * Takes a list of drivers data and published into the topic.
     * @param driversData - list of drivers data
     * @throws JsonProcessingException - throws when JSON has invalid format (Never occurs as the data model is validated at controller level).
     */
    public void sendDriversData(List<Driver> driversData) throws JsonProcessingException {
        this.producer.send(topicName, new ObjectMapper().writeValueAsString(driversData));
    }

}
