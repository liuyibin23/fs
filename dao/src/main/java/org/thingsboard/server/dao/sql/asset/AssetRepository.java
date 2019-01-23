package org.thingsboard.server.dao.sql.asset;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.thingsboard.server.dao.model.sql.AssetEntity;

import java.util.List;

@Repository
public interface AssetRepository extends JpaRepository<AssetEntity, String> {

    @Query("SELECT a FROM AssetEntity a")
    List<AssetEntity> findAll();

}
