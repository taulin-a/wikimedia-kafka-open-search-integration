package org.taulin.component.impl;

import lombok.extern.slf4j.Slf4j;
import org.taulin.component.RecentChangeEventConsumer;
import org.taulin.component.WikimediaKafkaConsumerRunner;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class WikimediaKafkaConsumerRunnerImpl implements WikimediaKafkaConsumerRunner {
    private final ScheduledExecutorService executorService;
    private final RecentChangeEventConsumer recentChangeEventConsumer;

    public WikimediaKafkaConsumerRunnerImpl(RecentChangeEventConsumer recentChangeEventConsumer) {
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.recentChangeEventConsumer = recentChangeEventConsumer;
    }

    @Override
    public void run() {
        executorService.scheduleWithFixedDelay(recentChangeEventConsumer::poll, 0L, 10L,
                TimeUnit.MILLISECONDS);
    }

    @Override
    public void close() throws Exception {
        terminateExecutor();
        recentChangeEventConsumer.close();
    }

    private void terminateExecutor() throws InterruptedException {
        executorService.shutdown();
        if (!executorService.awaitTermination(1000, TimeUnit.MILLISECONDS)) {
            log.info("Executor Service shut down");
        }
    }
}
