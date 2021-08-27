package com.zzc.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.reactor.IOReactorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

/**
 * @author Administrator
 */
@Slf4j
@Component
public class AsyncHttpUtils {

    private static CloseableHttpAsyncClient httpclient;

    @Autowired(required = false)
    private AsyncHttpUtils(final CloseableHttpAsyncClient httpclient) {
        if (Objects.isNull(httpclient)) {
            log.warn("init httpclient is null, please check!");
            try {
                AsyncHttpUtils.httpclient = Builder.buildAsyncHttpClient();
                AsyncHttpUtils.httpclient.start();
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    try {
                        AsyncHttpUtils.httpclient.close();
                    } catch (IOException e) {
                        log.warn("关闭httpclient异常", e);
                    }
                }));
            } catch (IOReactorException e) {
                throw new RuntimeException("初始化httpclient失败", e);
            }
        } else {
            AsyncHttpUtils.httpclient = httpclient;
        }
    }

    /**
     * Get请求
     */
    public static void get(final String url, final FutureCallback<HttpResponse> callback) {
        final HttpGet httpGet = new HttpGet(url);
        httpclient.execute(httpGet, callback);
    }

    private static class Builder {

        private final static int DEFAULT_CONNECT_TIMEOUT      = 3_000;
        private final static int DEFAULT_SOCKET_TIMEOUT       = 5_000;
        private final static int DEFAULT_CONNECT_POOL_TIMEOUT = -1;
        private final static int DEFAULT_IO_THREAD_COUNT      = Runtime.getRuntime().availableProcessors() * 5;
        private final static int DEFAULT_RECEIVE_BUFFER_SIZE  = 8 * 1024;
        private final static int DEFAULT_SEND_BUFFER_SIZE     = 8 * 1024;
        private final static int POOL_MAX_TOTAL               = 100;
        private final static int POOL_MAX_PER_ROUTE           = 10;

        private static CloseableHttpAsyncClient buildAsyncHttpClient() throws IOReactorException {
            return HttpAsyncClients
                    .custom()
                    .setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.INSTANCE)
                    .setConnectionManager(Builder.buildConnectionManager())
                    .setDefaultRequestConfig(RequestConfig
                                                     .custom()
                                                     .setSocketTimeout(DEFAULT_SOCKET_TIMEOUT)
                                                     .setConnectTimeout(DEFAULT_CONNECT_TIMEOUT)
                                                     .setConnectionRequestTimeout(DEFAULT_CONNECT_POOL_TIMEOUT)
                                                     .build())
                    .build();
        }

        private static PoolingNHttpClientConnectionManager buildConnectionManager() throws IOReactorException {
            final IOReactorConfig ioReactorConfig = IOReactorConfig
                    .custom()
                    .setRcvBufSize(DEFAULT_RECEIVE_BUFFER_SIZE)
                    .setSndBufSize(DEFAULT_SEND_BUFFER_SIZE)
                    .setSoTimeout(DEFAULT_SOCKET_TIMEOUT)
                    .setConnectTimeout(DEFAULT_CONNECT_TIMEOUT)
                    .setIoThreadCount(DEFAULT_IO_THREAD_COUNT)
                    .build();
            final PoolingNHttpClientConnectionManager connManager = new PoolingNHttpClientConnectionManager(new DefaultConnectingIOReactor(ioReactorConfig));
            connManager.setMaxTotal(POOL_MAX_TOTAL);
            connManager.setDefaultMaxPerRoute(POOL_MAX_PER_ROUTE);
            return connManager;
        }
    }
}
