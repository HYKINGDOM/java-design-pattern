package org.fkjava.user.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User implements Serializable {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(generator = "uuid2")
    @Column(length = 36)
    private String id;
    private String name;
    private String loginName;
    private String password;
    private String phone;
    private String tenantId;
    private String openId;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy年MM月dd日HH:mm:ss", locale = "GMT+8")
    private Date registeredTime;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy年MM月dd日HH:mm:ss", locale = "GMT+8")
    private Date passwordExpireTime;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy年MM月dd日HH:mm:ss", locale = "GMT+8")
    private Date accountExpireTime;
    private boolean accountNonLocked;
    private boolean enabled;

    @ManyToMany
    private List<Role> roles;

    @Transient
    public boolean isPasswordNonExpired() {
        return this.isNotExpired(passwordExpireTime);
    }

    @Transient
    public boolean isAccountNonExpired() {
        return this.isNotExpired(accountExpireTime);
    }

    private boolean isNotExpired(Date time) {
        if (time == null) {
            return true;
        } else {
            return time.getTime() > System.currentTimeMillis();
        }
    }
}
