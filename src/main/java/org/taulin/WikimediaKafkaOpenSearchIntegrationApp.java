package org.taulin;

import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.extern.slf4j.Slf4j;
import org.taulin.component.WikimediaKafkaConsumerRunner;
import org.taulin.factory.IntegrationModule;

@Slf4j
public class WikimediaKafkaOpenSearchIntegrationApp {
    public static void main(String... args) {
        final Injector injector = Guice.createInjector(new IntegrationModule());
        final WikimediaKafkaConsumerRunner wikimediaKafkaConsumerRunner = injector.getInstance(
                WikimediaKafkaConsumerRunner.class);
        wikimediaKafkaConsumerRunner.run();

        // shutdown hook
        final Thread mainThread = Thread.currentThread();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                log.info("Shutting down...");
                wikimediaKafkaConsumerRunner.close();

                mainThread.join();
            } catch (Exception e) {
                log.error("Error while shutting down: ", e);
                throw new RuntimeException(e);
            }
        }));
    }
}
