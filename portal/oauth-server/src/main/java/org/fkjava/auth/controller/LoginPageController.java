package org.fkjava.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.fkjava.commons.config.CommonConfigProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/uc/login")
public class LoginPageController {

    private final ObjectMapper objectMapper;
    private final CommonConfigProperties properties;

    public LoginPageController(ObjectMapper objectMapper, CommonConfigProperties properties) {
        this.objectMapper = objectMapper;
        this.properties = properties;
    }

    // 如果请求头的Accept包含了application/json，就会通过produces匹配请求。
    // produces表示服务器可以提供的数据类型。
    @RequestMapping(produces = {"application/json"})
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String noLogin() throws JsonProcessingException {

        Map<String, Object> map = new HashMap<>();
        map.put("code", "3");
        map.put("message", "还未登录");

        return objectMapper.writeValueAsString(map);
    }

    @RequestMapping
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> noLoginHTML(HttpSession session, HttpServletRequest request)
            throws IOException {
        String sessionId = session.getId();
        String prefix = "";
        if (properties.getApiPrefix().endsWith(properties.getApiPath())) {
            prefix = properties.getApiPath();
        }
        String username = request.getParameter("username");
        if (StringUtils.isEmpty(username)) {
            username = "";
        }
        String queryString = request.getQueryString();
        boolean hasError = false;
        if (queryString != null) {
            hasError = queryString.contains("error");
        }
        String error = "";
        if (hasError) {
            error = "登录失败";
        }

        URL url = this.getClass().getResource("/static/user-center/login.html");
        List<String> lines = IOUtils.readLines(url.openStream());
        StringBuilder builder = new StringBuilder();
        for (String line : lines) {
            builder.append(line);
        }
        String html = builder.toString();
        html = html.replace("{sessionId}", sessionId);
        html = html.replace("{prefix}", prefix);
        html = html.replace("{username}", username);
        html = html.replace("{error}", error);
        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.status(HttpStatus.OK);
        return bodyBuilder.body(html);
    }
}
