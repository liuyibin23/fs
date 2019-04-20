package org.thingsboard.server.fs.service;

import com.github.tobato.fastdfs.service.AppendFileStorageClient;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.thingsboard.server.fs.FsTestApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = FsTestApplication.class)
public class StorageClientTestBase {
    @Autowired
    protected AppendFileStorageClient storageClient;
}
