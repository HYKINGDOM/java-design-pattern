package org.fkjava.daily.sign.service;

import lombok.extern.slf4j.Slf4j;
import org.fkjava.commons.converter.DateToStringConverter;
import org.fkjava.commons.converter.StringToDateTimePropertyEditor;
import org.fkjava.commons.domain.Result;
import org.fkjava.daily.sign.domain.DailySignIn;
import org.fkjava.daily.sign.repository.DailySignInRepository;
import org.fkjava.event.domain.SignInEvent;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SignInServiceImpl implements SignInService {

    private final DailySignInRepository dailySignInRepository;
    private final RedisTemplate<Object, Object> redisTemplate;

    public SignInServiceImpl(
            DailySignInRepository dailySignInRepository,
            RedisTemplate<Object, Object> redisTemplate
    ) {
        this.dailySignInRepository = dailySignInRepository;
        this.redisTemplate = redisTemplate;
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

//            SignInEvent event = new SignInEvent();
//            event.setTime(signIn.getSignInTime());
//            event.setTimes(signIn.getTimes());
//            event.setType(signIn.getType().name());
//            event.setUserId(signIn.getUserId());

            // Date time, String userId, int times, String type
            SignInEvent event = new SignInEvent(signIn.getSignInTime(), userId, times, signIn.getType().name());

            this.redisTemplate.convertAndSend("event_daily_sign_in", event);
            return Result.ok();
        } else {
            return Result.error("今天已经签到");
        }
    }

    @Override
    public List<String> getSignInDays(String t, String userId, String d) {
        DailySignIn.Type type = DailySignIn.Type.valueOf(t);
        Date date = new Date();
        if (!StringUtils.isEmpty(d)) {
            // yyyy/MM/dd
            StringToDateTimePropertyEditor editor = new StringToDateTimePropertyEditor();
            date = editor.convert(d);
        }
        // 计算开始时间：1号所在周的第一天
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        // 往前推一个星期
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 7);
        Date startTime = calendar.getTime();


        // 下一个月的第一天
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        // 往后推迟一个星期
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 7);
        Date endTime = calendar.getTime();

        List<DailySignIn> signIns = this.dailySignInRepository
                .findByUserIdAndTypeAndSignInTimeBetween(userId, type, startTime, endTime);

        DateToStringConverter converter = new DateToStringConverter();
        return signIns.stream().map(dsi -> converter.convert(dsi.getSignInTime()))
                // 排序
//                .sorted((v1, v2)->v1.compareTo(v2))
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
    }
}
