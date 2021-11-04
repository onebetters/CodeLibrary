package com.zzc;

import com.alibaba.fastjson.parser.ParserConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello world!
 */
@Slf4j
@RestController
@EnableScheduling
@SpringBootApplication(scanBasePackages = {
        "com.zzc",
        "com.github.lwaddicor.springstartupanalysis"
}, exclude = {MultipartAutoConfiguration.class})
public class App {
    public static void main( String[] args ) {
        // 修复fastjson漏洞
        ParserConfig.getGlobalInstance().addAccept("com.zzc.");

        System.out.println( "Hello World!" );
    }
}
