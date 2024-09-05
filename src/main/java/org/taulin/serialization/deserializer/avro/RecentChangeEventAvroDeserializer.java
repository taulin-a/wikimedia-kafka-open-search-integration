package org.taulin.serialization.deserializer.avro;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.kafka.common.serialization.Deserializer;
import org.taulin.model.RecentChangeEvent;

import java.io.ByteArrayInputStream;

import org.apache.kafka.common.errors.SerializationException;

@Slf4j
public class RecentChangeEventAvroDeserializer implements Deserializer<RecentChangeEvent> {
    @Override
    public RecentChangeEvent deserialize(String topic, byte[] data) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data)) {
            Decoder binaryDecoder = DecoderFactory.get().binaryDecoder(byteArrayInputStream, null);

            SpecificDatumReader<RecentChangeEvent> reader = new SpecificDatumReader<>(RecentChangeEvent.getClassSchema());

            return reader.read(null, binaryDecoder);
        } catch (Exception e) {
            log.error("Error deserializing event", e);
            throw new SerializationException(e);
        }
    }
}
