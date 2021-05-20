package com.drivers.suggestion.kafka.producer;

import com.drivers.suggestion.config.SampleDataRetrieval;
import com.drivers.suggestion.controller.exceptionHandler.exceptions.GenericKafkaException;
import com.drivers.suggestion.model.Driver;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@SpringBootTest
@DirtiesContext
@RunWith(SpringRunner.class)
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
public class ProducerTest {
    private static String RECEIVER_TOPIC = "driver_location";

    @Autowired
    private Producer producer;

    @Autowired
    EmbeddedKafkaBroker embeddedKafkaBroker;

    @Test
    public void testSendDriversDataWithProperData() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Consumer<Integer, String> consumer = configureConsumer();
        producer.sendDriversData(SampleDataRetrieval.getSampleDrivers());

        ConsumerRecord<Integer, String> singleRecord = KafkaTestUtils.getSingleRecord(consumer, RECEIVER_TOPIC);

        Assertions.assertNotNull(singleRecord.value());
        List<Driver> receivedDriversList = mapper.readValue(singleRecord.value(), new TypeReference<List<Driver>>() {});

        Assertions.assertNotNull(receivedDriversList);
        Assertions.assertEquals(receivedDriversList.size(), 3);
        consumer.close();
    }

    @Test(expected = GenericKafkaException.class)
    public void testSendDriversDataWithNoData() {
        producer.sendDriversData(new ArrayList<>());
    }

    @Test(expected = GenericKafkaException.class)
    public void testSendDriversDataWithNull() {
        producer.sendDriversData(null);
    }

    private Consumer<Integer, String> configureConsumer() {
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps(RECEIVER_TOPIC, "true", embeddedKafkaBroker);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        Consumer<Integer, String> consumer = new DefaultKafkaConsumerFactory<Integer, String>(consumerProps)
                .createConsumer();
        consumer.subscribe(Collections.singleton(RECEIVER_TOPIC));
        return consumer;
    }
}