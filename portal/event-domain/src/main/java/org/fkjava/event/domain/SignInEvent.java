package org.fkjava.event.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SignInEvent extends Event {

    // 连续签到的次数
    private int times;
    // 签到类型
    private String type;

    // 为了满足JSON在序列化和反序列化的要求，所以需要加上无参构造器
    public SignInEvent() {
        super();
    }

    public SignInEvent(Date time, String userId, int times, String type) {
        super(SignInEvent.class.getName(), time, userId);
        this.times = times;
        this.type = type;
    }
}
