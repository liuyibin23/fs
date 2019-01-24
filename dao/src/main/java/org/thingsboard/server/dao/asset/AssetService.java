package org.thingsboard.server.dao.asset;

import org.thingsboard.server.dao.model.sql.AssetEntity;
import org.thingsboard.server.data.UserAssetOV;

import java.util.List;

public interface AssetService {

    List<AssetEntity> findAll();

    List<UserAssetOV> findUserAssetCount();
}
