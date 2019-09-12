package org.fkjava.verify.service.impl;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.fkjava.commons.config.ShortMessage;
import org.fkjava.commons.domain.Result;
import org.fkjava.verify.service.VerifyCodeService;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class VerifyCodeServiceImpl implements VerifyCodeService, InitializingBean, DisposableBean {

    private final ShortMessage shortMessage;
    private final RedisTemplate<String, String> stringRedisTemplate;
    private static final int DEFAULT_LENGTH = 4;
    private ObjectMapper objectMapper = new ObjectMapper();
    private IAcsClient client;

    /**
     * 验证码的长度，默认为4个字符
     */
    @Getter
    @Setter
    private int length = DEFAULT_LENGTH;

    public VerifyCodeServiceImpl(ShortMessage shortMessage, RedisTemplate<String, String> stringRedisTemplate) {
        this.shortMessage = shortMessage;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public void afterPropertiesSet() {
        DefaultProfile profile = DefaultProfile.getProfile(
                shortMessage.getAliYun().getRegionId(),
                shortMessage.getAliYun().getAccessKeyId(),
                shortMessage.getAliYun().getSecret());
        client = new DefaultAcsClient(profile);
    }

    @Override
    public void destroy() {
        client.shutdown();
    }

    @Override
    public Result send(String to) {
        String code = this.randomNumbers(getLength());
        Map<String, String> params = new LinkedHashMap<>();
        params.put("code", code);
        boolean status = this.sendSMS(to, params);
        if (status) {
            // 验证码在这里只是简单打印一下，实际上应该通过邮件或者短信发送出去
            log.trace("测试验证码: {}", code);
            BoundListOperations<String, String> ops = stringRedisTemplate.boundListOps("SMS_Verify_" + to);
            // 考虑到短信延迟的问题，允许用户收到多个短信验证码，任意一个验证码均可正常验证通过。
            ops.leftPush(code);
            // 130秒内有效，比页面提示的120秒多10秒。
            ops.expire(130, TimeUnit.SECONDS);
            return Result.ok();
        }
        return Result.error("短信验证码发送失败");
    }

    @Override
    public Result verify(String to, String code) {
        BoundListOperations<String, String> ops = stringRedisTemplate.boundListOps("SMS_Verify_" + to);
        Long size = ops.size();
        List<String> codes = ops.range(0, size == null ? 10 : size);
        if (codes == null) {
            return Result.error("验证码错误");
        }
        Result result = codes.stream()
                .filter(c -> c.equals(code))
                .map(m -> Result.ok())
                .findFirst()
                .orElse(Result.error("验证码错误"));

        // 当用户输入一次验证码以后，该手机号码中的全部验证码均失效
        ops.expire(1, TimeUnit.SECONDS);

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

    private boolean sendSMS(String to, Map<String, String> params) {
        // 把短信通过阿里云发送到用户手机
        ShortMessage.Template template = shortMessage.getAliYun().findByName("verify-code");

        CommonRequest request = new CommonRequest();
//        request.setProtocol(ProtocolType.HTTPS);
        request.setSysProtocol(ProtocolType.HTTPS);
//        request.setMethod(MethodType.POST);
        request.setSysMethod(MethodType.POST);
        // 短信服务器的域名
//        request.setDomain("dysmsapi.aliyuncs.com");
        request.setSysDomain("dysmsapi.aliyuncs.com");
//        request.setVersion(shortMessage.getAliYun().getVersion());
        request.setSysVersion(shortMessage.getAliYun().getVersion());
//        request.setAction("SendSms");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", shortMessage.getAliYun().getRegionId());
        request.putQueryParameter("PhoneNumbers", to);
        request.putQueryParameter("SignName", template.getSignName());
        request.putQueryParameter("TemplateCode", template.getId());
        try {
            String json = objectMapper.writeValueAsString(params);
            request.putQueryParameter("TemplateParam", json);
            CommonResponse response = client.getCommonResponse(request);
            log.trace("短信发送结果: {}", response.getData());
//            if (response.getData().contains("\"Message\":\"OK\"")) {
//                return true;
//            }
            return response.getData().contains("\"Message\":\"OK\"");
        } catch (ClientException | JsonProcessingException e) {
            log.error("短信发送的时候出现问题：" + e.getLocalizedMessage(), e);
            return false;
        }
    }
}
