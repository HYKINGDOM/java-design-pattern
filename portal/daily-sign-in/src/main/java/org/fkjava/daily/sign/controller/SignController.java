package org.fkjava.daily.sign.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.fkjava.commons.domain.Result;
import org.fkjava.daily.sign.domain.DailySignIn;
import org.fkjava.daily.sign.service.SignInService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

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

    @GetMapping
    public List<String> signInDays(
            @RequestParam(name = "date", required = false) String date,
            @RequestParam(name = "type", required = false) String type,
            @AuthenticationPrincipal Principal principal
    ) {
        if(StringUtils.isEmpty(type)){
            type = DailySignIn.Type.ON_DUTY.name();
        }
        String userId = principal.getName();
        return this.signInService.getSignInDays(type, userId, date);
    }
}
