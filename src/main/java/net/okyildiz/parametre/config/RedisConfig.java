package net.okyildiz.parametre.config;

import net.okyildiz.parametre.item.ParameterEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, ParameterEntity> redisTemplate(RedisConnectionFactory connectionFactory) {

        RedisTemplate<String, ParameterEntity> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Key ve HashKey için String Serializer kullanın
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // Value için JSON Serializer kullanın
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(ParameterEntity.class));
        template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(ParameterEntity.class));

        return template;
    }


}