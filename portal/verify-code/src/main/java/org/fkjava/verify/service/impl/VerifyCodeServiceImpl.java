package org.fkjava.verify.service.impl;

import org.fkjava.commons.domain.Result;
import org.fkjava.verify.service.VerifyCodeService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class VerifyCodeServiceImpl implements VerifyCodeService {

    // 临时使用一个Map存储，实际上应该存储到Redis或者MySQL中
    private Map<String, List<String>> map = new HashMap<>();

    @Override
    public Result send(String to) {
        String code = this.randomNumbers(4);
        // 验证码在这里只是简单打印一下，实际上应该通过邮件或者短信发送出去
        System.out.println("验证码: " + code);
        List<String> codes = map.get(to);
        if (codes == null) {
            codes = new LinkedList<>();
            map.put(to, codes);
        }
        codes.add(code);
        return Result.ok();
    }

    @Override
    public Result verify(String to, String code) {
        List<String> codes = map.get(to);
        if (codes == null) {
            return Result.error("验证码错误");
        }
        Result result = codes.stream()
                .filter(c -> c.equals(code))
                .map(m -> Result.ok())
                .findFirst()
                .orElse(Result.error("验证码错误"));
        return result;
    }

    private String randomNumbers(int length) {
        char[] cs = new char[length];
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            char c = (char) ('0' + random.nextInt(10));
            cs[i] = c;
        }
        return new String(cs);
    }

}
