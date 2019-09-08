package org.fkjava.verify.api;

import org.fkjava.commons.domain.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("verify-code")
public interface VerifyCodeService {

    /**
     * 发送验证码给用户
     *
     * @param to
     * @return
     */
    @PostMapping("/code")
    Result send(
            @RequestParam("to") String to
    );

    /**
     * 验证用户输入的验证码是否正确
     *
     * @param to
     * @param code
     * @return
     */
    @GetMapping("/verify")
    Result verify(
            @RequestParam("to") String to,
            @RequestParam("code") String code
    );
}
