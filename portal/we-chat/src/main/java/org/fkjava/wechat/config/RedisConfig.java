package org.fkjava.wechat.config;

import org.fkjava.wechat.message.EventMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.net.UnknownHostException;
import java.util.Collection;
import java.util.List;

@Configuration
public class RedisConfig {


//    @Bean
//    public Jaxb2Marshaller jaxb2Marshaller() {
//        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
//        marshaller.setPackagesToScan("org.fkjava");
//        return marshaller;
//    }

//    @Bean
//    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory)
//            throws UnknownHostException {
//        RedisTemplate<Object, Object> template = new RedisTemplate<>();
//        template.setConnectionFactory(redisConnectionFactory);
//
//        // 作为消息队列使用的时候需要设置的默认序列化程序
//        // 如果其他的场景（Hash、键值对）的时候，如果没有找到合适的序列化程序，也会使用此序列化程序
//        template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
//        // 使用OxmSerializer需要有Jaxb2Marshaller，而且需要放入Redis的对象需要在类上面增加@XmlRootElement注解，并且指定根元素名称
////        template.setDefaultSerializer(new OxmSerializer(jaxb2Marshaller(), jaxb2Marshaller()));
//
//
//        // 操作Hash的时候，用于序列化Hash对象
////        template.setHashValueSerializer(null);
//        // 用于序列化Hash对象的Key
////        template.setHashKeySerializer(null);
//
//        // 键值对操作的时候使用，用于序列化key
////        template.setKeySerializer(null);
//        // 键值对操作的时候使用，用于序列化value
////        template.setValueSerializer(null);
//        // 对字符串进行序列化的
////        template.setStringSerializer(new StringRedisSerializer());
//
//        return template;
//    }

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory)
            throws UnknownHostException {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        // 作为消息队列使用的时候需要设置的默认序列化程序
        // 如果其他的场景（Hash、键值对）的时候，如果没有找到合适的序列化程序，也会使用此序列化程序
        template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean
    public MessageListenerAdapter eventMessageListener() {
        MessageListenerAdapter adapter = new MessageListenerAdapter(this, "handleMessage");
        adapter.setSerializer(new GenericJackson2JsonRedisSerializer());
        adapter.setStringSerializer(new StringRedisSerializer());
        return adapter;
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory redisConnectionFactory
    ) {
        RedisMessageListenerContainer container
                = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);

        Collection<Topic> topics = List.of(new ChannelTopic("user-registered"), new PatternTopic("user-*"));
        container.addMessageListener(eventMessageListener(), topics);

        return container;
    }

    public void handleMessage(EventMessage message) {
        System.out.println("监听器收到的消息对象: " + message);
    }
}
