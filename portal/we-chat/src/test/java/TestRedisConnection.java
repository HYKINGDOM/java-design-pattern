import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;

// 测试完全基于客户端直接连接Redis数据库
public class TestRedisConnection {

    public static void main(String[] args) {
//        RedisClient client = RedisClient.create("redis://192.168.10.43:6379/1");

        // 相当于配置了JDBC的连接池
        RedisClient client = RedisClient.create("redis-sentinel://192.168.10.43:26379,192.168.10.43:26380,192.168.10.43:26381/0#master1");
        // 通过连接池获取连接
        StatefulRedisConnection<String, String> connection = client.connect();
        // 类似于JDBC的Statement
        RedisAsyncCommands<String, String> cmd = connection.async();
        // 执行操作
        cmd.set("key1", "value1");
    }
}
