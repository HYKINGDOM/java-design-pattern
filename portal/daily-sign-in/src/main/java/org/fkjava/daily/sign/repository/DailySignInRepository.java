package org.fkjava.daily.sign.repository;

import org.fkjava.daily.sign.domain.DailySignIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DailySignInRepository extends JpaRepository<DailySignIn, String> {
    List<DailySignIn> findByUserIdAndTypeAndSignInTimeBetween(String userId, DailySignIn.Type type, Date startTime, Date endTime);
}
