package org.thingsboard.server.admin;

import de.codecentric.boot.admin.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAdminServer
public class ApplicationAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationAdminApplication.class, args);
//        log.info("服务启动.....");
    }
}
