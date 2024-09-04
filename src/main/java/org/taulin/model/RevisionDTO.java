package org.taulin.model;

import lombok.Builder;

@Builder
public record RevisionDTO(
        Long old,
        Long nu) {
    @Override
    public String toString() {
        return "Revision{" +
                "old=" + old +
                ", nu=" + nu +
                '}';
    }
}
