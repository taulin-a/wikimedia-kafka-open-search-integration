package org.taulin.component.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.confluent.kafka.serializers.KafkaJsonDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.taulin.component.RecentChangeEventConsumer;
import org.taulin.model.RecentChangeEvent;

import java.time.Duration;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Properties;

@Slf4j
public class RecentChangeEventConsumerImpl implements RecentChangeEventConsumer {
    private final KafkaConsumer<Long, LinkedHashMap<String, Object>> recentChangeEventConsumer;
    private final Long pollTimeout;
    private final ObjectMapper objectMapper;

    @Inject
    public RecentChangeEventConsumerImpl(@Named("bootstrap.server") String bootstrapServer,
                                         @Named("topic.name") String topicName,
                                         @Named("group.id") String groupId,
                                         @Named("auto.offset.reset") String autoOffsetReset,
                                         @Named("poll.timeout") Long pollTimeout,
                                         ObjectMapper objectMapper) {
        final Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaJsonDeserializer.class.getName());

        this.recentChangeEventConsumer = new KafkaConsumer<>(properties);
        recentChangeEventConsumer.subscribe(Collections.singletonList(topicName));

        this.pollTimeout = pollTimeout;
        this.objectMapper = objectMapper;
    }

    @Override
    public void poll() {
        final ConsumerRecords<Long, LinkedHashMap<String, Object>> records = recentChangeEventConsumer.poll(
                Duration.ofMillis(pollTimeout));

        for (var record : records) {
            final RecentChangeEvent recentChangeEvent = linkedHashMapToRecentChangeEvent(record.value());
            log.info("Recent change event consumed: {}", recentChangeEvent);
            // TODO: Send to opensearch
        }
    }

    private RecentChangeEvent linkedHashMapToRecentChangeEvent(LinkedHashMap<String, Object> linkedHashMap) {
        return objectMapper.convertValue(linkedHashMap, RecentChangeEvent.class);
    }

    @Override
    public void close() {
        recentChangeEventConsumer.close();
        log.info("Consumer shut down");
    }
}
