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
@RequestMapping("/api/files")
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
    public FileResponseData uploadv2(MultipartFile file, HttpServletRequest request){
       return fileObjectService.uploadFile(file,request);
    }

    @RequestMapping(value = "/upload/base64/v2",method = RequestMethod.POST)
    public FileResponseData uplaodBase64v2(String file, String filename,HttpServletRequest request){
        return fileObjectService.uploadFile(file,filename,request);
    }

    @RequestMapping(value = "/delete/file",method = RequestMethod.POST)
    public FileResponseData deleteFile(String fileId,HttpServletRequest request){
        return fileObjectService.deleteFile(fileId,request);
    }

    @RequestMapping(value = "/test1",method = RequestMethod.GET)
    public String authTest1(){
        return "test1";
    }

//    @PreAuthorize("hasAnyAuthority('API')")
    @RequestMapping(value = "/test2",method = RequestMethod.GET)
    public String authTest2(){
        return "test2";
    }
}
