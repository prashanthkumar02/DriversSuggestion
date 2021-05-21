package com.drivers.suggestion.kafka.consumer;

import com.drivers.suggestion.config.SampleDataRetrieval;
import com.drivers.suggestion.kafka.producer.Producer;
import com.drivers.suggestion.service.impl.BaseService;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;

@SpringBootTest
@DirtiesContext
@RunWith(SpringRunner.class)
public class ConsumerTest {

    private static String RECEIVER_TOPIC = "driver_location";

    @Autowired
    private Consumer consumer;

    @Autowired
    private Producer producer;

    @Mock
    BaseService baseService;

    @ClassRule
    public static EmbeddedKafkaRule embeddedKafka = new EmbeddedKafkaRule(1, true, RECEIVER_TOPIC);

    @BeforeClass
    public static void setUpBeforeClass() {
        System.setProperty("spring.kafka.bootstrap-servers", embeddedKafka.getEmbeddedKafka().getBrokersAsString());
    }

    @Test
    public void testListenDriversDataWithProperData() {
        doNothing().when(baseService).insertDriverDetails(anyList());
        producer.sendDriversData(SampleDataRetrieval.getSampleDrivers());

        //verify(baseService, atLeast(1)).insertDriverDetails(anyList());
    }

}