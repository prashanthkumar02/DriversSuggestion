package com.drivers.suggestion.kafka.producer;

import com.drivers.suggestion.controller.exceptionHandler.exceptions.GenericKafkaException;
import com.drivers.suggestion.model.Driver;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Producer {

    @Value("${spring.kafka.common.topic}")
    String topicName;

    @Autowired
    private KafkaTemplate<String, String> producer;

    public void sendDriversData(List<Driver> driversData) {
        ObjectMapper mapper = new ObjectMapper();
        String dataInString;
        try {
            if(driversData == null || driversData.isEmpty()) {
                throw new GenericKafkaException("No data available to publish into Kafka");
            }

            dataInString = mapper.writeValueAsString(driversData);
            this.producer.send(topicName, dataInString);
        } catch (JsonProcessingException e) {
            throw new GenericKafkaException("Unhandled JSON Exception! further message - " + e.getMessage() );
        }
    }

}
