package org.taulin.component.impl;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.taulin.component.EventOpenSearchClient;
import org.taulin.component.RecentChangeEventConsumer;
import org.taulin.model.RecentChangeEvent;
import org.taulin.serialization.deserializer.avro.RecentChangeEventAvroDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

@Slf4j
public class RecentChangeEventConsumerImpl implements RecentChangeEventConsumer {
    private final KafkaConsumer<Long, RecentChangeEvent> recentChangeEventConsumer;
    private final Long pollTimeout;
    private final EventOpenSearchClient eventOpenSearchClient;

    @Inject
    public RecentChangeEventConsumerImpl(@Named("bootstrap.server") String bootstrapServer,
                                         @Named("topic.name") String topicName,
                                         @Named("group.id") String groupId,
                                         @Named("auto.offset.reset") String autoOffsetReset,
                                         @Named("poll.timeout") Long pollTimeout,
                                         EventOpenSearchClient eventOpenSearchClient) {
        final Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, RecentChangeEventAvroDeserializer.class.getName());
        properties.setProperty(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, "true");

        this.recentChangeEventConsumer = new KafkaConsumer<>(properties);
        recentChangeEventConsumer.subscribe(Collections.singletonList(topicName));

        this.pollTimeout = pollTimeout;
        this.eventOpenSearchClient = eventOpenSearchClient;
    }

    @Override
    public void poll() {
        final ConsumerRecords<Long, RecentChangeEvent> records = recentChangeEventConsumer.poll(
                Duration.ofMillis(pollTimeout));

        for (var record : records) {
            final RecentChangeEvent recentChangeEvent = record.value();
            log.info("Recent change event consumed: {}", recentChangeEvent);
            eventOpenSearchClient.addEventToBulk(recentChangeEvent);
        }

        eventOpenSearchClient.sendBulk();
    }

    @Override
    public void close() {
        recentChangeEventConsumer.close();
        log.info("Consumer shut down");
    }
}
