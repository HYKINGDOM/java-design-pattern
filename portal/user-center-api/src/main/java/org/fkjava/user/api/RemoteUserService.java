package org.fkjava.user.api;

import org.fkjava.user.domain.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("user-center")
public interface RemoteUserService {

    @GetMapping("/user/byLoginName")
    User byLoginName(@RequestParam("loginName") String loginName);

    @GetMapping("/user/byOpenId")
    User byOpenId(@RequestParam("openId") String openId);

    @GetMapping("/user/byId")
    User byId(@RequestParam("id") String id);

    @GetMapping("/user/byPhone")
    User byPhone(@RequestParam("phone") String phone);

    @GetMapping("/user/registry")
    User registry(User user);
}
