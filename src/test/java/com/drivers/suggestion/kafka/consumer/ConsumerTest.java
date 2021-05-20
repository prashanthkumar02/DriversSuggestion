package com.drivers.suggestion.kafka.consumer;

import com.drivers.suggestion.kafka.producer.Producer;
import com.drivers.suggestion.model.Driver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

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
        List<Driver> driverList = new ArrayList<>();

        Driver driver = new Driver();
        driver.setDriverId("testing");
        driver.setDriversLatitude(90);
        driver.setDriversLongitude(180);
        driverList.add(driver);

        producer.sendDriversData(driverList);
        // Need to have assertions in future
    }

}