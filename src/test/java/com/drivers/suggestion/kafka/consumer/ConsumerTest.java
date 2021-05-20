package com.drivers.suggestion.kafka.consumer;

import com.drivers.suggestion.config.SampleDataRetrieval;
import com.drivers.suggestion.kafka.producer.Producer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@DirtiesContext
@RunWith(SpringRunner.class)
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
public class ConsumerTest {

    private static String RECEIVER_TOPIC = "driver_location";

    @Autowired
    private Consumer consumer;

    @Autowired
    private Producer producer;

    @Test
    public void testListenDriversDataWithProperData() {
        producer.sendDriversData(SampleDataRetrieval.getSampleDrivers());

    }

}