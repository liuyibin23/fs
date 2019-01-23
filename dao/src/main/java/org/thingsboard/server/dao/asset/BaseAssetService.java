package org.thingsboard.server.dao.asset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thingsboard.server.dao.model.sql.AssetEntity;
import org.thingsboard.server.dao.sql.asset.AssetRepository;

import java.util.List;

@Service
public class BaseAssetService implements AssetService {

    @Autowired
    private AssetRepository assetRepository;

    @Override
    public List<AssetEntity> findAll() {
        return assetRepository.findAll();
    }
}
