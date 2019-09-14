package org.fkjava.daily.sign.service;

import lombok.extern.slf4j.Slf4j;
import org.fkjava.commons.domain.Result;
import org.fkjava.daily.sign.domain.DailySignIn;
import org.fkjava.daily.sign.repository.DailySignInRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class SignInServiceImpl implements SignInService {

    private final DailySignInRepository dailySignInRepository;

    public SignInServiceImpl(DailySignInRepository dailySignInRepository) {
        this.dailySignInRepository = dailySignInRepository;
    }

    @Override
    public Result sign(DailySignIn signIn) {
        // 1.检查当前用户是否已经签到，类型要匹配
        String userId = signIn.getUserId();
        // 得到当前时间的日历
        Calendar calendar = Calendar.getInstance();
        // 根据当前时间计算今天的开始和结束时间，然后查询当天是否有签到
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date startTime = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        Date endTime = calendar.getTime();

        log.trace("检查用户是否已经签到，用户ID: {}，开始时间: {}，结束时间: {}", userId, startTime, endTime);

        List<DailySignIn> signIns = this.dailySignInRepository
                .findByUserIdAndTypeAndSignInTimeBetween(userId, signIn.getType(), startTime, endTime);

        if (signIns.isEmpty()) {
            // 2.如果还未签到则新增一条签到的记录，否则返回错误
            signIn.setSignInTime(new Date());

            // 查询前一天的签到记录
            // 假设当前时间是9月15日
            // 还未减去1天的时候，日历此时是在9月16日
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 1);
            endTime = calendar.getTime();
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 1);
            startTime = calendar.getTime();
            List<DailySignIn> oldSignIns = this.dailySignInRepository
                    .findByUserIdAndTypeAndSignInTimeBetween(userId, signIn.getType(), startTime, endTime);

            int times = oldSignIns.stream()
                    .findFirst()
                    .map(DailySignIn::getTimes)
                    .orElse(0);
            times++;

            signIn.setTimes(times);
            this.dailySignInRepository.save(signIn);
            return Result.ok();
        } else {
            return Result.error("今天已经签到");
        }
    }
}
