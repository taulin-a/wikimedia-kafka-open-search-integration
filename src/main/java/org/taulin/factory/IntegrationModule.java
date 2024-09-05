package org.taulin.factory;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import lombok.extern.slf4j.Slf4j;
import org.taulin.component.EventOpenSearchClient;
import org.taulin.component.RecentChangeEventConsumer;
import org.taulin.component.WikimediaKafkaConsumerRunner;
import org.taulin.component.impl.EventOpenSearchClientImpl;
import org.taulin.component.impl.RecentChangeEventConsumerImpl;
import org.taulin.component.impl.WikimediaKafkaConsumerRunnerImpl;
import org.taulin.exception.ConfigurationException;
import org.taulin.mapper.RecentChangeEventMapper;
import org.taulin.mapper.RecentChangeEventMapperImpl;
import org.taulin.util.ResourceLoaderUtil;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

@Slf4j
public class IntegrationModule extends AbstractModule {
    @Override
    protected void configure() {
        Names.bindProperties(binder(), loadApplicationProperties());
        bind(RecentChangeEventMapper.class).to(RecentChangeEventMapperImpl.class);
        bind(EventOpenSearchClient.class).to(EventOpenSearchClientImpl.class);
        bind(RecentChangeEventConsumer.class).to(RecentChangeEventConsumerImpl.class);
        bind(WikimediaKafkaConsumerRunner.class).to(WikimediaKafkaConsumerRunnerImpl.class);
    }

    private Properties loadApplicationProperties() {
        try {
            Properties properties = new Properties();
            properties.load(new FileReader(ResourceLoaderUtil.loadResource("application.properties")));
            return properties;
        } catch (IOException ex) {
            log.error("Unable to load application.properties.");
            throw new ConfigurationException("Unable to load application properties configuration", ex);
        }
    }
}
