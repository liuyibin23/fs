package org.thingsboard.server.dao.model.sql;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.thingsboard.server.dao.model.BaseSqlEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
//@TypeDef(name = "json", typeClass = JsonStringType.class)
@Table(name = "customer")
public class CustomerEntity extends BaseSqlEntity {

    @Column(name = "tenant_id")
    private String tenantId;

    @Column(name = "title")
    private String title;

    @Column(name = "search_text")
    private String searchText;

    @Column(name = "country")
    private String country;

    @Column(name = "state")
    private String state;

    @Column(name = "city")
    private String city;

    @Column(name = "address")
    private String address;

    @Column(name = "address2")
    private String address2;

    @Column(name = "zip")
    private String zip;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

//    @Type(type = "json")
    @Column(name = "additional_info")
    private String additionalInfo;

    public CustomerEntity() {
        super();
    }

}
