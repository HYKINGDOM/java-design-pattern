package org.fkjava.wechat.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.fkjava.wechat.domain.WeixinErrorResponse;
import org.fkjava.wechat.message.send.OutMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 基于HTTP客户端的远程接口代理程序，在访问远程接口的时候，只需要传入URI、返回值类型、参数（Map）就可以访问远程接口。
 */
class HttpClientProxy {

    private static Logger log = LoggerFactory.getLogger(HttpClientProxy.class);
    private static final String BASE_URL = "https://api.weixin.qq.com/cgi-bin";
    private static final HttpClient client = HttpClient.newBuilder()
            .build();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static <T> T get(
            String uri,
            Class<T> returnType,
            Map<String, String> queryParams) {
        String url = buildUrl(uri, queryParams);

        // HttpClient是线程安全的，可以多个线程共享使用。
        HttpRequest request = HttpRequest.newBuilder()
                .GET()// 发送GET请求
                .uri(URI.create(url))//创建目标链接
                .build();

        // 发送请求
        try {
            HttpResponse<String> response = client
                    // send方法是同步方法，会有返回值的。
                    // client里面还有异步方法，没有返回值，通过回调来处理结果。
                    // 但是获取令牌的时候，需要返回值，所以同步调用方法。
                    // 调用其他接口的时候（比如发送信息、修改菜单）等，不需要返回值的就可以用异步方法。
                    .send(request,
                            // ofString返回的响应体转换为String
                            HttpResponse.BodyHandlers.ofString());

            String body = response.body();
            log.debug("远程接口返回的信息: \n{}", body);
            if (body.contains("errcode")) {
                // 返回错误信息
                throw OBJECT_MAPPER
                        .readValue(body, WeixinErrorResponse.class);
            } else {
                // 返回正常的JSON
                return OBJECT_MAPPER
                        .readValue(body, returnType);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 发送客服接口对应的消息
     *
     * @param uri
     * @param params
     * @param message
     */
    static void post(String uri, Map<String, String> params, OutMessage message) {
        try {
            String json = OBJECT_MAPPER.writeValueAsString(message);
            Map<String, ?> result = send(uri, params, json);

            Object errorCode = result.get("errcode");
            if (!errorCode.equals(0)) {
                log.error("发送信息给微信公众号平台出现问题，错误代码: {}，错误描述: {}", errorCode, result.get("errmsg"));
            }
        } catch (Exception e) {
            log.error("发送信息给微信客户端出现问题: " + e.getMessage(), e);
        }
    }

    /**
     * 通用的数据发送，具体的数据格式没有限定，根据微信公众号的数据格式构建一个Map即可。
     *
     * @param uri
     * @param params
     * @param data
     */
    static Map<String, ?> post(String uri, Map<String, String> params, Map<String, ?> data) {
        try {
            String json = OBJECT_MAPPER.writeValueAsString(data);
            return send(uri, params, json);
        } catch (JsonProcessingException e) {
            //log.error("发送信息给微信客户端出现问题: " + e.getMessage(), e);
            throw new RuntimeException("发送信息给微信客户端出现问题: " + e.getMessage(), e);
        }
    }

    private static Map<String, ?> send(String uri, Map<String, String> params, String json) {
        String url = buildUrl(uri, params);

        try {
            log.debug("POST发送时的请求体: \n{}", json);
            // HttpClient是线程安全的，可以多个线程共享使用。
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))// 发送POST请求
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .uri(URI.create(url))//创建目标链接
                    .build();

            HttpResponse<String> response = client
                    .send(request,
                            HttpResponse.BodyHandlers.ofString());

            String body = response.body();
            log.debug("POST发送数据返回结果: \n{}", body);

//            if (body.contains("errcode")) {
//                WeixinErrorResponse error = OBJECT_MAPPER
//                        .readValue(body, WeixinErrorResponse.class);
//                log.error("发送信息给微信公众号平台出现问题，错误代码: {}，错误描述: {}", error.getErrorCode(), error.getErrorMessage());
//            }
            Map<String, ?> result = OBJECT_MAPPER.readValue(body, HashMap.class);
            return result;
        } catch (IOException | InterruptedException e) {
//            log.error("发送信息给微信客户端出现问题: " + e.getMessage(), e);
            throw new RuntimeException("发送信息给微信客户端出现问题: " + e.getMessage(), e);
        }
    }

    private static String buildUrl(String uri, Map<String, String> params) {
        // 拼接请求的URL
        StringBuilder builder = new StringBuilder(BASE_URL);
        builder.append(uri);
        if (!params.isEmpty()) {
            builder.append("?");
            // 把参数以键值对方式拼接在URL的后面
            params.forEach((key, value) -> builder.append(key)
                    .append("=")
                    .append(value)
                    .append("&"));
            // 把最后一个&去掉
            builder.deleteCharAt(builder.length() - 1);
        }

        String url = builder.toString();
        log.debug("完整的URL: {}", url);
        return url;
    }
}
