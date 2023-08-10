package com.job.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.StringSerializer;
import java.util.Properties;

public class App
{
    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main( String[] args )
    {
        log.info("Kafka Producer App");

        log.debug("Creating Kafka Producer...");
        String bootstrapServers = "kafka:9092";
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        log.debug("Sending message...");
        for (int i = 0; i < 1000000; i++) {
            int topic_index = 1;
            if (i % 2 == 0) {
                topic_index = 2;
            }
            String topic_name = "topic" +  topic_index;
            String event = "event " + i;
            log.info("Sending event: " + event + " to topic: " + topic_name);
            ProducerRecord<String, String> producerRecord =
                    new ProducerRecord<>(topic_name, event);

            producer.send(producerRecord);
        }
        producer.flush();
        producer.close();
    }
}
