package org.fkjava.wechat.repository.redis;

import org.fkjava.wechat.domain.AccessToken;
import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.stereotype.Repository;

// 由于JPA和Redis都使用接口的方式来简化DAO的开发，此时要注意：JPA的程序不能扫描到Redis的DAO，Redis的扫描只能扫描Redis的DAO
@Repository
public interface AccessTokenRepository extends KeyValueRepository<AccessToken, String> {
}
