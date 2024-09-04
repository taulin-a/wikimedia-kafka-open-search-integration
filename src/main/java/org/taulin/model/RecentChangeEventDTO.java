package org.taulin.model;

import lombok.Builder;

@Builder
public record RecentChangeEventDTO(
        String schema,
        MetaDTO meta,
        Long id,
        String type,
        Integer namespace,
        String title,
        String titleUrl,
        String comment,
        Long timestamp,
        String user,
        Boolean bot,
        String notifyUrl,
        String serverUrl,
        String serverName,
        String serverScriptPath,
        String wiki,
        String parsedComment,
        Boolean minor,
        Boolean patrolled,
        RevisionDTO length,
        RevisionDTO revision) {
    @Override
    public String toString() {
        return "RecentChangeEvent{" +
                "schema='" + schema + '\'' +
                ", meta=" + meta +
                ", id=" + id +
                ", type='" + type + '\'' +
                ", namespace=" + namespace +
                ", title='" + title + '\'' +
                ", titleUrl='" + titleUrl + '\'' +
                ", comment='" + comment + '\'' +
                ", timestamp=" + timestamp +
                ", user='" + user + '\'' +
                ", bot=" + bot +
                ", notifyUrl='" + notifyUrl + '\'' +
                ", serverUrl='" + serverUrl + '\'' +
                ", serverName='" + serverName + '\'' +
                ", serverScriptPath='" + serverScriptPath + '\'' +
                ", wiki='" + wiki + '\'' +
                ", parsedComment='" + parsedComment + '\'' +
                ", minor=" + minor +
                ", patrolled=" + patrolled +
                ", length=" + length +
                ", revision=" + revision +
                '}';
    }
}
