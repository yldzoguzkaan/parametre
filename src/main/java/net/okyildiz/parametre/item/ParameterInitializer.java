package net.okyildiz.parametre.item;

import jakarta.annotation.PostConstruct;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ParameterInitializer {

    private final ParameterRepository parameterRepository;
    private final RedisTemplate<String, ParameterEntity> redisTemplate;
    private static final String REDIS_KEY = "parameters";

    public ParameterInitializer(ParameterRepository parameterRepository, RedisTemplate<String, ParameterEntity> redisTemplate) {
        this.parameterRepository = parameterRepository;
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void loadParametersIntoRedis() {

        // Redis'teki tüm verileri temizle
        redisTemplate.getConnectionFactory().getConnection().flushAll();
        System.out.println("Tüm redis parametreleri silindi.");

        // Veritabanından tüm nesneleri çek
        List<ParameterEntity> allObjects = parameterRepository.findAll();

        // Nesneleri type değerlerine göre grupla
        Map<String, List<ParameterEntity>> objectsByType = allObjects.stream()
                .collect(Collectors.groupingBy(ParameterEntity::getType));

        // Gruplanmış nesneleri Redis'e kaydet
        objectsByType.forEach((type, objects) -> {
            objects.forEach(obj -> {
                redisTemplate.opsForHash().put("MY_OBJECTS_BY_TYPE:" + type, obj.getUid(), obj);
            });
        });

        System.out.println(allObjects.size() + " adet parametre redise başarıyla yüklendi.");
    }

}
