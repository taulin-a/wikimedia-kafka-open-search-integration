package org.taulin.component.impl;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.opensearch.client.RestClient;
import org.opensearch.client.json.jackson.JacksonJsonpMapper;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch.core.BulkRequest;
import org.opensearch.client.opensearch.core.bulk.BulkOperation;
import org.opensearch.client.opensearch.core.bulk.IndexOperation;
import org.opensearch.client.transport.rest_client.RestClientTransport;
import org.taulin.component.EventOpenSearchClient;
import org.taulin.model.RecentChangeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class EventOpenSearchClientImpl implements EventOpenSearchClient {
    private static final String DEFAULT_INDEX = "wikimedia-event";

    private final OpenSearchClient client;
    private final List<BulkOperation> operationList = new ArrayList<>();

    @Inject
    public EventOpenSearchClientImpl(@Named("opensearch.protocol") String protocol,
                                     @Named("opensearch.host") String host,
                                     @Named("opensearch.port") Integer port,
                                     @Named("opensearch.user") String user,
                                     @Named("opensearch.pass") String password) {
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(user, password));

        //Initialize the client with SSL and TLS enabled
        RestClient restClient = RestClient.builder(new HttpHost(host, port, protocol)).
                setHttpClientConfigCallback(httpClientBuilder ->
                        httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider))
                .build();
        RestClientTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());

        this.client = new OpenSearchClient(transport);
    }

    @Override
    public void addEventToBulk(RecentChangeEvent recentChangeEvent) {
        IndexOperation<RecentChangeEvent> indexOperation = new IndexOperation.Builder<RecentChangeEvent>()
                .index(DEFAULT_INDEX)
                .id(Objects.nonNull(recentChangeEvent.id())
                        ? Long.toString(recentChangeEvent.id())
                        : null)
                .document(recentChangeEvent)
                .build();

        operationList.add(new BulkOperation.Builder()
                .index(indexOperation)
                .build());
    }

    @Override
    public void sendBulk() {
        if (operationList.isEmpty()) return;

        try {
            BulkRequest bulkRequest = new BulkRequest.Builder()
                    .index(DEFAULT_INDEX)
                    .operations(operationList)
                    .build();

            client.bulk(bulkRequest);
        } catch (Exception e) {
            log.error("Error while indexing event bulk in opensearch: ", e);
        } finally {
            operationList.clear();
        }
    }
}
