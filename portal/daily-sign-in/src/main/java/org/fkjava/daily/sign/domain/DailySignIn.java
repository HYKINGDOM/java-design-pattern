package org.fkjava.daily.sign.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(indexes = {
        @Index(name = "findByUserIdAndTypeAndSignInTimeBetween", columnList = "userId,type,signInTime")
})
public class DailySignIn {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(generator = "uuid2")
    @Column(length = 36)
    private String id;

    // 谁签到
    @Column(length = 36)
    private String userId;

    // 签到的时间
    @Temporal(TemporalType.TIMESTAMP)
    private Date signInTime;

    // 第几次签到
    private int times;

    // 签到类型
    // 默认枚举存储ORDINAL（索引），如果枚举发生变化，索引就不那么准确
    // 存储String则是存储枚举的常量名，不管枚举如何变化，结果都是可靠的
    @Enumerated(EnumType.STRING)
    private Type type = Type.ON_DUTY;

    public static enum Type {
        /**
         * 上班
         */
        ON_DUTY,
        /**
         * 下班
         */
        OFF_DUTY,
        /**
         * 加班
         */
        OUT_TIME
    }
}