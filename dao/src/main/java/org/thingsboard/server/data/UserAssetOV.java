package org.thingsboard.server.data;

import lombok.Data;

@Data
public class UserAssetOV {
    String name;
    long assetNum;
    public UserAssetOV(){}
    public UserAssetOV(String name,long assetNum){
        this.name = name;
        this.assetNum = assetNum;
    }
}
