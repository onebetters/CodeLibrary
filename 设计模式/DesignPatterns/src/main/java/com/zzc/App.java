package com.zzc;

import com.alibaba.fastjson.parser.ParserConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 *
 * @author Administrator
 */
@SpringBootApplication
public class App {
    public static void main( String[] args ) {
        // 修复fastjson漏洞
        ParserConfig.getGlobalInstance().addAccept("com.zzc.");

        System.out.println( "Hello World!" );
    }
}
