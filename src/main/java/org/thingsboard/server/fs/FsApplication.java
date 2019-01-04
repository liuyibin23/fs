package org.thingsboard.server.fs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class FsApplication {

    public static void main(String[] args) {
        SpringApplication.run(FsApplication.class, args);
    }

}

