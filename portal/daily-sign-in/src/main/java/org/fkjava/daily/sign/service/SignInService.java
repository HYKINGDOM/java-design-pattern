package org.fkjava.daily.sign.service;

import org.fkjava.commons.domain.Result;
import org.fkjava.daily.sign.domain.DailySignIn;

import java.util.List;

public interface SignInService {
    Result sign(DailySignIn signIn);

    List<String> getSignInDays(String type, String userId, String date);
}
