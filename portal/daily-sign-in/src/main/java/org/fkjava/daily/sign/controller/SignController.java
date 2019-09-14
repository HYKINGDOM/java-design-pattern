package org.fkjava.daily.sign.controller;

import org.fkjava.commons.domain.Result;
import org.fkjava.daily.sign.domain.DailySignIn;
import org.fkjava.daily.sign.service.SignInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/sign")
public class SignController {

    private final SignInService signInService;

    public SignController(SignInService signInService) {
        this.signInService = signInService;
    }

    @PostMapping()
    public Result sign(
            @RequestBody DailySignIn signIn,
            @AuthenticationPrincipal Principal principal
    ) {
        signIn.setUserId(principal.getName());
        return this.signInService.sign(signIn);
    }
}
