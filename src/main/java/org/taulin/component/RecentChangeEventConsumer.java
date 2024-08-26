package org.taulin.component;

public interface RecentChangeEventConsumer extends AutoCloseable {
    void poll();
}