package org.fkjava.wechat.controller;

import org.fkjava.user.api.RemoteUserService;
import org.fkjava.user.domain.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class RegistryCompleteController {

    private final RemoteUserService userService;

    public RegistryCompleteController(RemoteUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registry/complete")
    public ModelAndView complete(@AuthenticationPrincipal Principal principal) {
        String id = principal.getName();
        User user = userService.byId(id);
        ModelAndView mav = new ModelAndView("user/registry/complete");
        mav.addObject("user", user);
        return mav;
    }
}
