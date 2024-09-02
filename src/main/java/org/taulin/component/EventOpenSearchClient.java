package org.taulin.component;

import org.taulin.model.RecentChangeEvent;

public interface EventOpenSearchClient {
    void addEventToBulk(RecentChangeEvent recentChangeEvent);

    void sendBulk();
}
