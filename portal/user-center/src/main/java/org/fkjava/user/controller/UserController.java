package org.fkjava.user.controller;

import org.fkjava.user.domain.User;
import org.fkjava.user.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/byName")
    public User byName(@RequestParam("username") String username) {
        return userService.loadByLoginName(username);
    }

    @GetMapping("/byLoginName")
    public User byLoginName(@RequestParam("loginName") String loginName) {
        return userService.loadByLoginName(loginName);
    }

    @GetMapping("/byId")
    public User byId(@RequestParam("id") String id) {
        return userService.loadById(id);
    }

    @GetMapping("/byOpenId")
    public User byOpenId(@RequestParam("openId") String openId) {
        return userService.loadByOpenId(openId);
    }

    @GetMapping({"", "/user-info"})
    public User userInfo(@AuthenticationPrincipal Principal principal) {
        return userService.loadById(principal.getName());
    }

    @GetMapping("/byPhone")
    public User byPhone(@RequestParam("phone") String phone) {
        return this.userService.loadByPhone(phone);
    }

    @PostMapping("/registry")
    public User registry(@RequestBody User user) {
        return userService.save(user);
    }
}
