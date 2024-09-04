package org.taulin.serialization.deserializer.avro;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;
import org.taulin.model.RecentChangeEvent;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.apache.kafka.common.errors.SerializationException;

@Slf4j
public class RecentChangeEventAvroDeserializer implements Deserializer<RecentChangeEvent> {
    @Override
    public RecentChangeEvent deserialize(String topic, byte[] data) {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(data))) {
            return (RecentChangeEvent) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.error("Error deserializing event", e);
            throw new SerializationException(e);
        }
    }
}
