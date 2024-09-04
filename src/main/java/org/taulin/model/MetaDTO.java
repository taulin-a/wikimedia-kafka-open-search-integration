package org.taulin.model;

public record MetaDTO(
        String uri,
        String requestId,
        String id,
        String dt,
        String domain,
        String stream,
        String topic,
        Integer partition,
        Long offset) {
    @Override
    public String toString() {
        return "Meta{" +
                "uri='" + uri + '\'' +
                ", requestId='" + requestId + '\'' +
                ", id='" + id + '\'' +
                ", dt='" + dt + '\'' +
                ", domain='" + domain + '\'' +
                ", stream='" + stream + '\'' +
                ", topic='" + topic + '\'' +
                ", partition=" + partition +
                ", offset=" + offset +
                '}';
    }
}
