package org.thingsboard.server.fs.util.fastdfsClient;

import com.github.tobato.fastdfs.conn.FdfsWebServer;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.exception.FdfsUnsupportStorePathException;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FilenameUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.servlet.MultipartConfigElement;
import java.io.*;
import java.nio.charset.Charset;

@Component
@Slf4j
public class FastDFSClientWrapper {

    /**
     * 路径分隔符
     */
    public static final String SEPARATOR = "/";
    /**
     * Point
     */
    public static final String POINT = ".";

    @Autowired
    private FastFileStorageClient storageClient;

    @Autowired
    private FdfsWebServer fdfsWebServer;

//    @Value("${spring.http.multipart.max-file-size}")
//    private int maxFileSize;
    @Autowired
    private MultipartConfigElement multipartConfigElement;
    /**
     * 上传文件
     * @param file 文件对象
     * @return 文件访问地址
     * @throws IOException
     */
    @Deprecated
    public String uploadFile(MultipartFile file) throws IOException {
        StorePath storePath = storageClient.uploadFile(file.getInputStream(),file.getSize(), FilenameUtils.getExtension(file.getOriginalFilename()),null);
        return getResAccessUrl(storePath);
    }

    /**
     * 将一段字符串生成一个文件上传
     * @param content 文件内容
     * @param fileExtension
     * @return
     */
    @Deprecated
    public String uploadFile(String content, String fileExtension) {
        byte[] buff = content.getBytes(Charset.forName("UTF-8"));
        ByteArrayInputStream stream = new ByteArrayInputStream(buff);
        StorePath storePath = storageClient.uploadFile(stream,buff.length, fileExtension,null);
        return getResAccessUrl(storePath);
    }

    @Deprecated
    public String uploadBase64Img(String base64Content,String fileExtension) throws IOException {
        BASE64Decoder decoder = new BASE64Decoder();
        //Base64解码
        byte[] buff = decoder.decodeBuffer(base64Content);
        for (int i = 0; i < buff.length; ++i){
            if (buff[i] < 0){//调整异常数据
                buff[i] += 256;
            }
        }
        ByteArrayInputStream stream = new ByteArrayInputStream(buff);
        StorePath storePath = storageClient.uploadFile(stream,buff.length, fileExtension,null);
        return getResAccessUrl(storePath);
    }

    /**
     * MultipartFile 上传文件
     * @param file MultipartFile
     * @return 返回上传成功后的文件id
     * @throws FastDFSException
     */
    public String uploadFileWithMultipart(MultipartFile file) throws FastDFSException {
        return upload(file);
    }

    /**
     * 使用 MultipartFile 上传
     * @param file MultipartFile
     * @return 文件的fileid
     * @throws FastDFSException file为空则抛出异常
     */
    private String upload(MultipartFile file)throws FastDFSException{
        if(file == null || file.isEmpty()){
            throw new FastDFSException(ErrorCode.FILE_ISNULL.CODE, ErrorCode.FILE_ISNULL.MESSAGE);
        }
        String path = null;
        try {
            path = upload(file.getInputStream(), file.getOriginalFilename());
        } catch (IOException e) {
            e.printStackTrace();
            throw new FastDFSException(ErrorCode.FILE_ISNULL.CODE, ErrorCode.FILE_ISNULL.MESSAGE);
        }
        return path;
    }

    /**
     * 上传base64文件
     * @param base64
     * @param filename 文件名
     * @return 文件的fileid
     * @throws FastDFSException
     */
    private String upload(String base64,String filename) throws FastDFSException {
        if(org.apache.commons.lang3.StringUtils.isBlank(base64)){
            throw new FastDFSException(ErrorCode.FILE_ISNULL.CODE, ErrorCode.FILE_ISNULL.MESSAGE);
        }
        return upload(new ByteArrayInputStream(Base64.decodeBase64(base64)), filename);
    }

    /**
     * 上传通用方法
     * @param is 文件输入流
     * @param filename 文件名
     * @return 上传成功后的fileid ，如：group1/M00/00/00/wKgz6lnduTeAMdrcAAEoRmXZPp870.jpeg
     * @throws FastDFSException
     */
    private String upload(InputStream is, String filename) throws FastDFSException {
        if(is == null){
            throw new FastDFSException(ErrorCode.FILE_ISNULL.CODE, ErrorCode.FILE_ISNULL.MESSAGE);
        }

//        try {
//            if(is.available() > maxFileSize){
//                throw new FastDFSException(ErrorCode.FILE_OUT_SIZE.CODE, ErrorCode.FILE_OUT_SIZE.MESSAGE);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        try {
            if(is.available() > multipartConfigElement.getMaxFileSize()){
                throw new FastDFSException(ErrorCode.FILE_OUT_SIZE.CODE, ErrorCode.FILE_OUT_SIZE.MESSAGE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        filename = toLocal(filename);
        // 返回路径
        String path = null;
        // 文件名后缀
        String suffix = getFilenameSuffix(filename);

        try {
            StorePath storePath = storageClient.uploadFile(is,is.available(), suffix,null);
            path = storePath.getFullPath();
            if(org.apache.commons.lang3.StringUtils.isBlank(path)) {
                throw new FastDFSException(ErrorCode.FILE_UPLOAD_FAILED.CODE, ErrorCode.FILE_UPLOAD_FAILED.MESSAGE);
            }
            if (log.isDebugEnabled()) {
                log.debug("upload file success, return path is {}", path);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new FastDFSException(ErrorCode.FILE_UPLOAD_FAILED.CODE, ErrorCode.FILE_UPLOAD_FAILED.MESSAGE);
        } finally {
            // 关闭流
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return path;
    }

    private String getResAccessUrl(StorePath storePath) {
        String fileUrl = fdfsWebServer.getWebServerUrl() +"/"+ storePath.getFullPath();
        return fileUrl;
    }

    /**
     * 删除文件
     * @param fileUrl 文件访问地址
     * @return
     */
    public void deleteFile(String fileUrl) {
        if (StringUtils.isEmpty(fileUrl)) {
            return;
        }
        try {
            StorePath storePath = StorePath.praseFromUrl(fileUrl);
            storageClient.deleteFile(storePath.getGroup(), storePath.getPath());
        } catch (FdfsUnsupportStorePathException e) {
            log.warn(e.getMessage());
        }
    }
    public boolean executeDownloadFile(CloseableHttpClient httpClient, String remoteFileUrl, String localFilePath, String charset, boolean closeHttpClient) throws ClientProtocolException, IOException {
        CloseableHttpResponse response = null;
        InputStream in = null;
        FileOutputStream fout = null;
        try {
            HttpGet httpget = new HttpGet(remoteFileUrl);
            response = httpClient.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return false;
            }
            in = entity.getContent();
            File file = new File(localFilePath);
            fout = new FileOutputStream(file);
            int l;
            byte[] tmp = new byte[1024];
            while ((l = in.read(tmp)) != -1) {
                fout.write(tmp, 0, l);
                // 注意这里如果用OutputStream.write(buff)的话，图片会失真
            }
            // 将文件输出到本地
            fout.flush();
            EntityUtils.consume(entity);
            return true;
        } finally {
            // 关闭低层流。
            if (fout != null) {
                try {
                    fout.close();
                } catch (Exception e) {
                }
            }
            if (response != null) {
                try {
                    response.close();
                } catch (Exception e) {
                }
            }
            if (closeHttpClient && httpClient != null) {
                try {
                    httpClient.close();
                } catch (Exception e) {
                }
            }
        }
    }


    /**
     * 获取文件名称的后缀
     *
     * @param filename 文件名 或 文件路径
     * @return 文件后缀
     */
    public static String getFilenameSuffix(String filename) {
        String suffix = null;
        String originalFilename = filename;
        if (org.apache.commons.lang3.StringUtils.isNotBlank(filename)) {
            if (filename.contains(SEPARATOR)) {
                filename = filename.substring(filename.lastIndexOf(SEPARATOR) + 1);
            }
            if (filename.contains(POINT)) {
                suffix = filename.substring(filename.lastIndexOf(POINT) + 1);
            } else {
                if (log.isErrorEnabled()) {
                    log.error("filename error without suffix : {}", originalFilename);
                }
            }
        }
        return suffix;
    }

    /**
     * 转换路径中的 '\' 为 '/' <br>
     * 并把文件后缀转为小写
     *
     * @param path 路径
     * @return
     */
    public static String toLocal(String path) {
        if (org.apache.commons.lang3.StringUtils.isNotBlank(path)) {
            path = path.replaceAll("\\\\", SEPARATOR);

            if (path.contains(POINT)) {
                String pre = path.substring(0, path.lastIndexOf(POINT) + 1);
                String suffix = path.substring(path.lastIndexOf(POINT) + 1).toLowerCase();
                path = pre + suffix;
            }
        }
        return path;
    }
}
