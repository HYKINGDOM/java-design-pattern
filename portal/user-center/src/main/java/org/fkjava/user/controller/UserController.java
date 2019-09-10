package org.fkjava.user.controller;

import org.fkjava.commons.domain.Result;
import org.fkjava.user.domain.User;
import org.fkjava.user.service.UserService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
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
        User user = this.userService.loadById(id);
        user.setPassword(null);
        return user;
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

    @Secured(value = "ADMIN")
    @PostMapping("set-login-name")
    public Result setLoginName(
            @RequestParam("id") String id,
            @RequestParam("loginName") String loginName) {
        return this.userService.updateLoginName(id, loginName);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("update-phone")
    public Result updatePhone(
            @RequestParam("id") String id,
            @RequestParam("phone") String phone,
            @RequestParam("verifyCode") String verifyCode
    ) {
        return this.userService.updatePhone(id, phone, verifyCode);
    }

    @Secured(value = "ROLE_ADMIN")
    @PostMapping("account-non-locked")
    public Result updateAccountNonLocked(
            @RequestParam("id") String id,
            @RequestParam("accountNonLocked") boolean accountNonLocked) {
        return this.userService.updateAccountNonLocked(id, accountNonLocked);
    }

    @RolesAllowed("ROLE_ADMIN")
    @PostMapping("enabled")
    public Result updateEnabled(
            @RequestParam("id") String id,
            @RequestParam("enabled") boolean enabled) {
        return this.userService.updateEnabled(id, enabled);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("update-password")
    public Result updatePassword(
            @RequestParam("id") String id,
            @RequestParam("password") String password
    ) {
        return this.userService.updatePassword(id, password);
    }
}
