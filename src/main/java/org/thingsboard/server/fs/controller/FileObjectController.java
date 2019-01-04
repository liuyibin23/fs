package org.thingsboard.server.fs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thingsboard.server.fs.service.FileObjectService;
import org.thingsboard.server.fs.util.fastdfsClient.FastDFSClientWrapper;
import org.thingsboard.server.fs.util.fastdfsClient.FileResponseData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class FileObjectController {

    @Autowired
    private FastDFSClientWrapper dfsClient;

    @Autowired
    private FileObjectService fileObjectService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public String upload(MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String fileUrl = dfsClient.uploadFile(file);
        return fileUrl;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @RequestMapping(value = "/upload/base64",method = RequestMethod.POST)
    public String uploadBase64Img(String file, String fileExtension,HttpServletRequest request, HttpServletResponse response)throws IOException{
        String fileUrl = dfsClient.uploadBase64Img(file,fileExtension);
        return fileUrl;
    }

    @RequestMapping(value = "/upload/v2",method = RequestMethod.POST)
    @ResponseBody
    public FileResponseData uploadv2(MultipartFile file, HttpServletRequest request){
       return fileObjectService.uploadFile(file,request);
    }

}
