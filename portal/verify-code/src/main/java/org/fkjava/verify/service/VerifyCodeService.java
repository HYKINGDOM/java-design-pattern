package org.fkjava.verify.service;

import org.fkjava.commons.domain.Result;

public interface VerifyCodeService {
    Result send(String to);

    Result verify(String to, String code);
}
