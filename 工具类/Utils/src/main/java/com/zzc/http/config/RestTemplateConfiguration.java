package com.zzc.http.config;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultClientConnectionReuseStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 */
@Configuration
public class RestTemplateConfiguration {
    @Bean("pwbRestTemplate")
    public RestTemplate restTemplate() {
        RestTemplate template = new RestTemplate();
        initRestTemplate(template);
        return template;
    }

    public static void initRestTemplate(RestTemplate template) {
        List<HttpMessageConverter<?>> converters = template.getMessageConverters();
        for (int i = 0, size = converters.size(); i < size; i++) {
            if (converters.get(i) instanceof StringHttpMessageConverter) {
                converters.remove(i);
                converters.add(i, new StringHttpMessageConverter(StandardCharsets.UTF_8));
                break;
            }
        }

        // 连接池
        HttpClient httpClient = HttpClientBuilder.create()
                .setMaxConnTotal(512)
                .setMaxConnPerRoute(512)
                .evictIdleConnections(1200, TimeUnit.SECONDS)
                .setConnectionReuseStrategy(DefaultClientConnectionReuseStrategy.INSTANCE)
                .setConnectionTimeToLive(180, TimeUnit.SECONDS)
                .build();

        // 连接超时
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        factory.setConnectionRequestTimeout(3000);
        factory.setConnectTimeout(3000);
        factory.setReadTimeout(15000);

        template.setRequestFactory(factory);
    }
}
