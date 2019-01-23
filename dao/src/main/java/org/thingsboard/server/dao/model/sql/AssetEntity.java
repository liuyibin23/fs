package org.thingsboard.server.dao.model.sql;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "asset")
public class AssetEntity {

    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "additional_info")
    private String additionalInfo;
    @Column(name = "customer_id")
    private String customerId;
    @Column(name = "name")
    private String name;
    @Column(name = "search_text")
    private String searchText;
    @Column(name = "tenant_id")
    private String tenantId;
    @Column(name = "type")
    private String type;
}
