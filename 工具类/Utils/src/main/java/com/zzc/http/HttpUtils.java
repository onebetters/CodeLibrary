package com.zzc.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zzc.http.config.RestTemplateConfiguration;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import java.net.URI;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Administrator
 */
@Slf4j
@Component
public class HttpUtils {

    @Getter
    private static RestTemplate restTemplate = new RestTemplate();

    static {
        RestTemplateConfiguration.initRestTemplate(restTemplate);
    }

    @Resource
    @Qualifier("pwbRestTemplate")
    public void setRestTemplate(RestTemplate template) {
        restTemplate = template;
    }

    public static String get(String url) {
        return get(url, String.class);
    }

    public static <T> T get(final String url, Class<T> clazz) {
        return restTemplate.getForObject(encodeUrl(url), clazz);
    }

    /**
     * get请求
     *
     * @param url          请求地址
     * @param responseType 响应类型
     * @param uriVariables url参数。(k,v) -> k=v
     */
    public static  <T> T get(final String url, final Class<T> responseType, final Map<String, Object> uriVariables) {
        String urlUse = encodeUrl(url).toString();
        if (MapUtils.isNotEmpty(uriVariables)) {
            final String parameters = uriVariables.keySet()
                    .stream()
                    .filter(StringUtils::isNotBlank)
                    .map(StringUtils::trim)
                    .map(k -> k + "={" + k + "}")
                    .collect(Collectors.joining("&"));
            if (StringUtils.isNotBlank(parameters)) {
                urlUse += (StringUtils.contains(urlUse, "?") ? parameters : "?" + parameters);
            }
        }
        return restTemplate.getForObject(urlUse, responseType, uriVariables);
    }

    @SuppressWarnings("UnusedDeclaration")
    public <T> T post(final String url, final Object body, final Class<T> clazz) {
        return restTemplate.postForObject(encodeUrl(url), body, clazz);
    }

    @SuppressWarnings("UnusedDeclaration")
    public static byte[] postForByte(String url, Map<String, Object> map) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<Map<String, Object>> request        = new HttpEntity<>(map, headers);
        ResponseEntity<byte[]>          responseEntity = restTemplate.postForEntity(url, request, byte[].class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity.getBody();
        } else {
            log.error("请求出错, url={}, 错误码: {}", url, responseEntity.getStatusCode());
            throw new Exception("请求出错");
        }
    }

    public static String postJson(final String url, final Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>((JSONObject) JSON.toJSON(body), headers);
        return restTemplate.postForEntity(encodeUrl(url), requestEntity, String.class).getBody();
    }

    public static <T> T postJson(final String url, final Object body, final Class<T> clazz) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>((JSONObject) JSON.toJSON(body), headers);
        return restTemplate.postForEntity(encodeUrl(url), requestEntity, clazz).getBody();
    }

    private static URI encodeUrl(String url) {
        return UriComponentsBuilder.fromHttpUrl(url).build().encode().toUri();
    }
}
