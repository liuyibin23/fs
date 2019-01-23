package org.thingsboard.server.dao.model;

import java.util.UUID;

public interface BaseEntity {
    UUID getId();

    void setId(UUID id);
}
