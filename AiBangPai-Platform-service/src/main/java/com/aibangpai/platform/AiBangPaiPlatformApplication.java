package com.aibangpai.platform;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class AiBangPaiPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiBangPaiPlatformApplication.class, args);
    }

}
