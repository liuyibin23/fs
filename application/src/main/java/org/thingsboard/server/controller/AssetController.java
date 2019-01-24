package org.thingsboard.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.thingsboard.server.dao.asset.AssetService;
import org.thingsboard.server.dao.model.sql.AssetEntity;
import org.thingsboard.server.data.UserAssetOV;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AssetController {

    @Autowired
    protected AssetService assetService;

    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    @ResponseBody
    public String helloTest(){
        return "hello assets";
    }

    @RequestMapping(value = "/asset/all",method = RequestMethod.GET)
    public List<AssetEntity> getAllEntity(){
        return assetService.findAll();
    }

    @RequestMapping(value = "/asset/user/count",method = RequestMethod.GET)
    public List<UserAssetOV> getUserAssetCount(){
        return assetService.findUserAssetCount();
    }

}
