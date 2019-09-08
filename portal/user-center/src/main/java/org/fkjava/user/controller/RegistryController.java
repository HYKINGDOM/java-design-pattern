package org.fkjava.user.controller;

import org.fkjava.commons.domain.Result;
import org.fkjava.user.domain.User;
import org.fkjava.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/registry")
public class RegistryController {

    @Autowired
    private UserService userService;

    @PostMapping(consumes = "application/json")
    public Result registry(@RequestBody User user, @RequestParam("code") String code) {
        return this.userService.registry(user, code);
    }

    @PostMapping
    public Result registry(
            @RequestParam("name") String name,
            @RequestParam("loginName") String loginName,
            @RequestParam("password") String password,
            @RequestParam("phone") String phone,
            @RequestParam("phone") String tenantId,
            @RequestParam("code") String code) {
        User user = new User();
        user.setLoginName(loginName);
        user.setName(name);
        user.setPassword(password);
        user.setPhone(phone);
        user.setTenantId(tenantId);
        return this.registry(user, code);
    }
}
