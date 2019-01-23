package org.thingsboard.server.dao.model.sql;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "attribute_kv")
public class AttributeKvEntity {

    @EmbeddedId
    private AttributeKvCompositeKey id;

    @Column(name = "bool_v")
    private Boolean booleanValue;

    @Column(name = "str_v")
    private String strValue;

    @Column(name = "long_v")
    private Long longValue;

    @Column(name = "dbl_v")
    private Double doubleValue;

    @Column(name = "last_update_ts")
    private Long lastUpdateTs;

}
