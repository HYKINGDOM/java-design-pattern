package org.fkjava.verify.controller;

import org.fkjava.commons.domain.Result;
import org.fkjava.verify.service.VerifyCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VerifyCodeController {

    @Autowired
    private VerifyCodeService verifyCodeService;

    @RequestMapping("/send")
    public Result send(@RequestParam("to") String to) {
        return verifyCodeService.send(to);
    }

    @RequestMapping("/verify")
    public Result verify(@RequestParam("to") String to, @RequestParam("code") String code) {
        return verifyCodeService.verify(to, code);
    }
}
