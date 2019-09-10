package org.fkjava.user.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

@Getter
@Setter
public class AuthUserDetails extends User {

    private String id;
    private String name;
    private String loginName;
    private String phone;
    private String tenantId;
    private String openId;
    @JsonFormat(pattern = "yyyy年MM月dd日HH:mm:ss", locale = "GMT+8")
    private Date registeredTime;
    @JsonFormat(pattern = "yyyy年MM月dd日HH:mm:ss", locale = "GMT+8")
    private Date passwordExpireTime;
    @JsonFormat(pattern = "yyyy年MM月dd日HH:mm:ss", locale = "GMT+8")
    private Date accountExpireTime;

    public AuthUserDetails() {
        this(null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                Collections.emptyList());
    }

    public AuthUserDetails(
            String id,
            String name,
            String loginName,
            String password,
            String phone,
            String tenantId,
            String openId,
            Date registeredTime,
            Date passwordExpireTime,
            Date accountExpireTime,
            Boolean enabled,
            Boolean accountNonLocked,
            Collection<? extends GrantedAuthority> authorities) {
        super(
                id,
                password,
                enabled,
                isNotExpired(accountExpireTime),
                isNotExpired(passwordExpireTime),
                accountNonLocked,
                authorities
        );
        this.id = id;
        this.name = name;
        this.loginName = loginName;
        this.phone = phone;
        this.tenantId = tenantId;
        this.openId = openId;
        this.registeredTime = registeredTime;
        this.passwordExpireTime = passwordExpireTime;
        this.accountExpireTime = accountExpireTime;
    }

    private static boolean isNotExpired(Date time) {
        if (time == null) {
            return true;
        } else {
            return time.getTime() > System.currentTimeMillis();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append(": ");
        sb.append("Id: ").append(this.id).append("; ");
        sb.append("OpenId: ").append(this.openId).append("; ");
        sb.append("TenantId: ").append(this.tenantId).append("; ");
        sb.append("LoginName: ").append(this.loginName).append("; ");
        sb.append("Name: ").append(this.name).append("; ");
        sb.append("Phone: ").append(this.phone).append("; ");
        sb.append("AccountExpireTime: ").append(this.accountExpireTime).append("; ");
        sb.append("PasswordExpireTime: ").append(this.passwordExpireTime).append("; ");
        sb.append("RegisteredTime: ").append(this.registeredTime).append("; ");

        sb.append("Username: ").append(super.getUsername()).append("; ");
        sb.append("Password: [PROTECTED]; ");
        sb.append("Enabled: ").append(super.isEnabled()).append("; ");
        sb.append("AccountNonExpired: ").append(super.isAccountNonExpired()).append("; ");
        sb.append("credentialsNonExpired: ").append(super.isCredentialsNonExpired())
                .append("; ");
        sb.append("AccountNonLocked: ").append(super.isAccountNonLocked()).append("; ");

        if (!super.getAuthorities().isEmpty()) {
            sb.append("Granted Authorities: ");

            boolean first = true;
            for (GrantedAuthority auth : super.getAuthorities()) {
                if (!first) {
                    sb.append(",");
                }
                first = false;

                sb.append(auth);
            }
        } else {
            sb.append("Not granted any authorities");
        }

        return sb.toString();
    }
}
