package org.thingsboard.server.fs.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.proto.storage.DownloadByteArray;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.thingsboard.server.fs.domain.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Set;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

/**
 * 支持断点续传文件操作测试
 */
@Slf4j
public class StorageClientAppendFileTest extends StorageClientTestBase {

    /**
     * 支持断点续传的文件操作测试
     *
     * @throws IOException
     */
    @Test
    public void testAppendFileStorageClient() throws IOException {
        String firstString = "This is a string of append file Test.";
        String secendString = " This is secend content.";

        log.debug("##上传第一段..##");
        RandomTextFile file = new RandomTextFile(firstString);
//        StorePath path = storageClient.uploadAppenderFile(TestConstants.DEFAULT_GROUP, file.getInputStream(),
//                file.getFileSize(), file.getFileExtName());
        StorePath path = storageClient.uploadAppenderFile(null, file.getInputStream(),
                file.getFileSize(), file.getFileExtName());
        assertNotNull(path);
        log.debug("上传文件 result={}", path);

        log.debug("##添加第二段..##");
        RandomTextFile secendFile = new RandomTextFile(secendString);
        storageClient.appendFile(path.getGroup(), path.getPath(), secendFile.getInputStream(),
                secendFile.getFileSize());

        log.debug("##下载文件..##");
        DownloadByteArray callback = new DownloadByteArray();
        byte[] content = storageClient.downloadFile(path.getGroup(), path.getPath(), callback);
        byte[] fullContent = getContent(file, secendFile);
        // 验证文件相同
        assertArrayEquals(content, fullContent);

        log.debug("##截取保留第一段文件..##");
        long truncatedFileSize = 0;
        // TODO truncatedFileSize 是任何其他值都会报错?
        storageClient.truncateFile(path.getGroup(), path.getPath(), truncatedFileSize);

        log.debug("##使用modify重新上传第一段文件..##");
        storageClient.modifyFile(path.getGroup(), path.getPath(), file.getInputStream(), file.getFileSize(), 0);
        log.debug("##使用modify重新上传第二段文件..##");
        storageClient.modifyFile(path.getGroup(), path.getPath(), secendFile.getInputStream(), secendFile.getFileSize(),
                file.getFileSize());
        log.debug("##下载文件..##");
        content = storageClient.downloadFile(path.getGroup(), path.getPath(), callback);
        // 验证文件相同
        assertArrayEquals(content, fullContent);

        log.debug("##删除文件..##");
        storageClient.deleteFile(path.getGroup(), path.getPath());

    }

    /**
     * 合并文件内容
     *
     * @param file
     * @param secendFile
     * @return
     */
    private byte[] getContent(RandomTextFile file, RandomTextFile secendFile) {
        int fileSize = (int) file.getFileSize();
        int secendFileSize = (int) secendFile.getFileSize();
        byte[] fullContent = new byte[fileSize + secendFileSize];
        System.arraycopy(file.toByte(), 0, fullContent, 0, fileSize);
        System.arraycopy(secendFile.toByte(), 0, fullContent, fileSize, secendFileSize);
        return fullContent;
    }

    @Test
    public void testAppendFileStorageClientByFile() throws Exception {

        File file = TestUtils.getFile(TestConstants.XIAO_JIE_JIE_IMG);
        int splitSize = 10*1024;//10k
        log.debug("##初始化文件存储区..##");
        Date now = new Date();
        StorePath path = initFileStorage(file.length(),splitSize,"jpg");
        log.debug("初始化文件时间：" + ((new Date()).getTime() - now.getTime()));

        log.debug("##分割文件..##");
        Set<FileEntity> fileEntities = SplitFileUtils.splitFile(file.getAbsolutePath(),splitSize);

        log.debug("##分割文件上传..##");
        now = new Date();
        for (FileEntity fileEntity:fileEntities) {
            byte[] fileByte = fileEntity.getFileChunk();
            storageClient.modifyFile(path.getGroup(),path.getPath(),new ByteArrayInputStream(fileByte),fileEntity.getLength(),fileEntity.getFileOffset());
        }
        log.debug("上传文件时间：" + ((new Date()).getTime() - now.getTime()));
        log.debug("##下载文件..##");
        DownloadByteArray callback = new DownloadByteArray();
        byte[] content = storageClient.downloadFile(path.getGroup(), path.getPath(), callback);
        byte[] srcContent = FileUtils.readFileToByteArray(file);
        // 验证文件相同
        assertArrayEquals(content, srcContent);

        log.debug("##删除文件..##");
        storageClient.deleteFile(path.getGroup(), path.getPath());
    }

    /**
     * 初始化一块指定大小的文件存储区
     */
    private StorePath initFileStorage(long srcSize,int splitSize,String fileExtName){
        int totalPart = (int)(srcSize / splitSize);
        totalPart = srcSize % splitSize == 0 ? totalPart : totalPart + 1;
        StorePath path = null;
        for(int i = 0; i < totalPart; i++){
            if(srcSize > splitSize){
                if(i == 0){
                    byte[] bufferByte = new byte[splitSize];
                    path = storageClient.uploadAppenderFile(null, new ByteArrayInputStream(bufferByte),
                            bufferByte.length, fileExtName);
                } else {
                    long remainingSize = srcSize - (i * splitSize);
                    if(remainingSize > splitSize){
                        byte[] bufferByte = new byte[splitSize];
                        storageClient.appendFile(path.getGroup(),path.getPath(),new ByteArrayInputStream(bufferByte),bufferByte.length);
                    } else {
                        byte[] bufferByte = new byte[(int)remainingSize];
                        storageClient.appendFile(path.getGroup(),path.getPath(),new ByteArrayInputStream(bufferByte),bufferByte.length);
                    }
                }
            } else {
                byte[] bufferByte = new byte[(int)srcSize];
                path = storageClient.uploadAppenderFile(null, new ByteArrayInputStream(bufferByte),
                        bufferByte.length, fileExtName);
            }
        }
        return path;
    }

}
