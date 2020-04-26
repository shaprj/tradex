package ru.shaprj.util;
/*
 * Created by O.Shalaevsky on 26.04.2020
 */

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.function.Consumer;

public class MessageBrokerConsumers {

    /*
     *
     * Consume Apache Kafka message broker
     *
     * */

    public static void subscribeKafkaTopic(String topicName, String groupId, String clientId,
                                           Consumer<String> resultConsumer) {

        Properties props = new Properties();

        props.put("bootstrap.servers", "192.168.1.16:9092");
        props.put("group.id", groupId);
        props.put("client.id", clientId);
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        consumer.subscribe(Arrays.asList(topicName));

        startPolling(100, consumer, resultConsumer);
    }

    private static void startPolling(int periodMills,
                                     KafkaConsumer<String, String> kafkaConsumer,
                                     Consumer<String> resultConsumer) {
        do {

            ConsumerRecords<String, String> records =
                    kafkaConsumer.poll(Duration.ofMillis(periodMills));

            if (!records.isEmpty()) {

                records.forEach(record -> {
                    resultConsumer.accept(record.value());
                });

            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } while (true);
    }

}
