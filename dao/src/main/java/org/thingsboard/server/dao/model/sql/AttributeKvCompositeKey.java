package org.thingsboard.server.dao.model.sql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.thingsboard.server.dao.model.EntityType;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class AttributeKvCompositeKey implements Serializable {

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_type")
    private EntityType entityType;
    @Column(name = "entity_id")
    private String entityId;
    @Column(name = "attribute_type")
    private String attributeType;
    @Column(name = "attribute_key")
    private String attributeKey;

}
