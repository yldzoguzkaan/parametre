package net.okyildiz.parametre.item;

import jakarta.annotation.PostConstruct;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class ParameterInitializer {

    private final ParameterRepository parameterRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String REDIS_KEY = "parameters";

    public ParameterInitializer(ParameterRepository parameterRepository, RedisTemplate<String, Object> redisTemplate) {
        this.parameterRepository = parameterRepository;
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void loadParametersIntoRedis() {
        Set<String> keys = redisTemplate.keys("*");
        if (keys != null) {
            redisTemplate.delete(keys);
        }
        System.out.println("Redis cache temizlendi.");

        List<ParameterEntity> parameters = parameterRepository.findAll();

        if (parameters.isEmpty()) {
            System.out.println("Veritabanında yüklenecek parametre bulunamadı.");
            return;
        }

        for (ParameterEntity parameter : parameters) {
            if (redisTemplate.opsForValue().get(parameter.getUid()) == null) {
                redisTemplate.opsForValue().set(parameter.getUid(), parameter);
                System.out.println("RedisInitializer: Redis'e yeni veri eklendi - " + parameter.getUid());
            }
        }
        System.out.println("RedisInitializer: Veritabanındaki tüm veriler Redis'e yüklendi.");
    }

}
