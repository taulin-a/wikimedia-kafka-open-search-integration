package org.taulin.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.taulin.model.Meta;
import org.taulin.model.MetaDTO;
import org.taulin.model.RecentChangeEvent;
import org.taulin.model.RecentChangeEventDTO;
import org.taulin.model.Revision;
import org.taulin.model.RevisionDTO;

@Mapper
public interface RecentChangeEventMapper {
    @Mapping(source = "schema$", target = "schema")
    RecentChangeEventDTO recentChangeEventToRecentChangeEventDto(RecentChangeEvent recentChangeEvent);

    MetaDTO metaToMetaDto(Meta meta);

    RevisionDTO revisionToRevisionDto(Revision revision);

    default String charSequenceToString(CharSequence charSequence) {
        return (String) charSequence;
    }
}
