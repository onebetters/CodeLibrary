package com.zzc;

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
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
    }
}
