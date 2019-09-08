package org.fkjava.wechat.message;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

// 需要先安装lombok插件，然后在pom.xml文件中增加lombok的依赖
// 如果程序员自己提供了get、set方法，Lombok不会自动生成
@Getter
@Setter
@NoArgsConstructor//生成无参构造器
@AllArgsConstructor//所有字段作为一个构造器
@ToString(callSuper = true)
public class EventMessage extends Message {

    @JsonProperty("Event")
    private String event;
    @JsonProperty("EventKey")
    private String eventKey;
    @JsonProperty("Ticket")
    private String ticket;

    @JsonProperty("Latitude")
    private double latitude;
    @JsonProperty("Longitude")
    private double longitude;
    @JsonProperty("Precision")
    private double precision;
}
