package org.fkjava.daily.sign.service;

import org.fkjava.commons.domain.Result;
import org.fkjava.daily.sign.domain.DailySignIn;

public interface SignInService {
    Result sign(DailySignIn signIn);
}
