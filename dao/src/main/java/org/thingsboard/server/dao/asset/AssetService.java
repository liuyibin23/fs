package org.thingsboard.server.dao.asset;

import org.thingsboard.server.dao.model.sql.AssetEntity;

import java.util.List;

public interface AssetService {

    List<AssetEntity> findAll();

}
