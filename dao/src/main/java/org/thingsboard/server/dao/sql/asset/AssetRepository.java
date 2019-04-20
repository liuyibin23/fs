package org.thingsboard.server.dao.sql.asset;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.thingsboard.server.dao.model.sql.AssetEntity;
import org.thingsboard.server.data.UserAssetOV;

import java.util.List;

@Repository
public interface AssetRepository extends JpaRepository<AssetEntity, String> {

    @Query("SELECT a FROM AssetEntity a")
    List<AssetEntity> findAll();

    //JPQL查询
    @Query("SELECT new org.thingsboard.server.data.UserAssetOV(u.firstName,COUNT(a.id)) FROM AssetEntity a,UserEntity u " +
            "WHERE a.tenantId = u.tenantId GROUP BY u.firstName")
    List<UserAssetOV> findUserAssetCount();

    //原始SQL查询
    @Query(value = "SELECT b.first_name ,COUNT(a.id) as asset_num FROM " +
            "asset as a , tb_user as b WHERE a.tenant_id = b.tenant_id GROUP BY b.first_name",
            nativeQuery = true)
    List<Object[]> findUserAssetCountNative();
}
