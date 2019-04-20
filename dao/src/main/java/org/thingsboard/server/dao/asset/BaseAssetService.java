package org.thingsboard.server.dao.asset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thingsboard.server.dao.model.sql.AssetEntity;
import org.thingsboard.server.dao.sql.asset.AssetRepository;
import org.thingsboard.server.data.UserAssetOV;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BaseAssetService implements AssetService {

    @Autowired
    private AssetRepository assetRepository;

    @Override
    public List<AssetEntity> findAll() {
        return assetRepository.findAll();
    }

    @Override
    public List<UserAssetOV> findUserAssetCount() {
        return assetRepository.findUserAssetCountNative()
                .stream()
                .map(item -> new UserAssetOV(item[0].toString(),Long.parseLong(item[1].toString()) ))
                .collect(Collectors.toList());
//        return assetRepository.findUserAssetCount();
    }

    @Override
    public List<UserAssetOV> findUserAssetCount2() {
        return assetRepository.findUserAssetCount();
    }
}
