package org.thingsboard.server.dao.model;

import lombok.Data;
import org.thingsboard.server.data.UUIDConverter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

@Data
@MappedSuperclass
public class BaseSqlEntity implements BaseEntity {
    @Id
    @Column(name = "id")
    protected String id;

    @Override
    public UUID getId() {
        if (id == null) {
            return null;
        }
        return UUIDConverter.fromString(id);
    }

    public void setId(UUID id) {
        this.id = UUIDConverter.fromTimeUUID(id);
    }

    protected UUID toUUID(String src){
        return UUIDConverter.fromString(src);
    }

    protected String toString(UUID timeUUID){
        return UUIDConverter.fromTimeUUID(timeUUID);
    }
}
