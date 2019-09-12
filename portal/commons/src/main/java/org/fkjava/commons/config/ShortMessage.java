package org.fkjava.commons.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

//      region-id: cn-hangzhou
//              access-key-id: LTAIab2ajidnpUNj
//              secret: B0XGcFiv87fOhLBdnsHi7DCXUPqG9q
//              version: 2017-05-25
//              templates:
//              verify-code:
//              id: SMS_90645001
//              sign-name: 疯狂软件
@Getter
@Setter
@ConfigurationProperties(prefix = "fk.short-messages")
public class ShortMessage {

    private ShortMessage.AliYun aliYun;

    @Getter
    @Setter
    public static class AliYun implements InitializingBean {
        private String regionId = "default";
        private String accessKeyId;
        private String secret;
        private String version = "2017-05-25";
        private Map<String, Template> templates = new HashMap<>();

        public Template findByName(String name) {
            if (!templates.containsKey(name)) {
                throw new RuntimeException("阿里云短信配置名称 " + name + " 不存在！");
            }
            return templates.get(name);
        }

        @Override
        public void afterPropertiesSet() {
            templates.forEach((key, template) -> template.setName(key));
        }
    }

    @Getter
    @Setter
    public static class Template {
        private String name;
        private String id;
        private String signName = "疯狂软件";
    }
}
