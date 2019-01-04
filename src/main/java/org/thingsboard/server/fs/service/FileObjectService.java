package org.thingsboard.server.fs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thingsboard.server.fs.util.fastdfsClient.FastDFSClientWrapper;
import org.thingsboard.server.fs.util.fastdfsClient.FastDFSException;
import org.thingsboard.server.fs.util.fastdfsClient.FileResponseData;

import javax.servlet.http.HttpServletRequest;

@Service
public class FileObjectService {

    @Autowired
    private FastDFSClientWrapper dfsClient;

    /**
     * 文件服务器地址
     */
    @Value("${fdfs.web-server-url}")
    private String fileServerAddr;

    /**
     * 上传通用方法，只上传到服务器，不保存记录到数据库
     * @param file
     * @param request
     * @return
     */
    public FileResponseData uploadFile(MultipartFile file, HttpServletRequest request){
        FileResponseData responseData = new FileResponseData();
        try {
            // 上传到服务器
            String filepath = dfsClient.uploadFileWithMultipart(file);

            responseData.setFileName(file.getOriginalFilename());
            responseData.setFilePath(filepath);
            responseData.setFileType(FastDFSClientWrapper.getFilenameSuffix(file.getOriginalFilename()));

            responseData.setHttpUrl(fileServerAddr+"/"+filepath);
        } catch (FastDFSException e) {
            e.printStackTrace();
            responseData.setSuccess(false);
            responseData.setCode(e.getCode());
            responseData.setMessage(e.getMessage());
        }

        return responseData;
    }

}
