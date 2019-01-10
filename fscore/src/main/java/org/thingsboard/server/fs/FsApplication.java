package org.thingsboard.server.fs;

import de.codecentric.boot.admin.config.EnableAdminServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
@EnableAdminServer
@Slf4j
public class FsApplication {

    public static void main(String[] args) {
        SpringApplication.run(FsApplication.class, args);
        log.info("服务启动.....");
    }

}

