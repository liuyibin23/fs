package org.thingsboard.server.dao.model.sql;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;
import org.thingsboard.server.dao.model.BaseSqlEntity;
import org.thingsboard.server.data.security.Authority;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tb_user")
public class UserEntity extends BaseSqlEntity {

    @Column(name = "tenant_id")
    private String tenantId;

    @Column(name = "customer_id")
    private String customerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "authority")
    private Authority authority;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "search_text")
    private String searchText;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "additional_info")
    private String additionalInfo;

}
