package org.taulin.component;

import org.taulin.model.RecentChangeEvent;

public interface EventOpenSearchClient {
    void sendEvent(RecentChangeEvent recentChangeEvent);
}
