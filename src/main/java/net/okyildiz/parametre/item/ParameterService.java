package net.okyildiz.parametre.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.okyildiz.parametre.utils.BaseService;
import net.okyildiz.parametre.utils.GenericResultResponse;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ParameterService extends BaseService {

    private final ParameterRepository parameterRepository;
    private final RedisTemplate<String, ParameterEntity> redisTemplate;
    private final HashOperations<String, String, ParameterEntity> hashOperations;
    private static final String KEY_PREFIX = "MY_OBJECTS_BY_TYPE:";

    public ParameterService(ParameterRepository parameterRepository, RedisTemplate<String, ParameterEntity> redisTemplate) {
        this.parameterRepository = parameterRepository;
        this.redisTemplate = redisTemplate;
        hashOperations = redisTemplate.opsForHash();
    }

    public GenericResultResponse createParameter(ParameterDTO dto) {
        GenericResultResponse response = new GenericResultResponse();

        try{
            ParameterEntity parameter = (ParameterEntity) mergeObjects(new ParameterEntity(),dto);
            setBaseColumnsForCreate(parameter,"001"); //tokendan gelen user bilgisi için kullanılmak üzere eklendi.
            parameter = parameterRepository.save(parameter);
            response.setData(mergeObjects(dto,parameter));
        }catch (Exception e) {
            response.setStatus(-200);
            response.setMessage("Error creating parameter.");
            response.setMessageDetail(e.getMessage());
        }
        return response;
    }

    public GenericResultResponse updateParameter(String UID, ParameterDTO dto) {
        GenericResultResponse response = new GenericResultResponse();
        try{
            ParameterEntity parameter = parameterRepository.findById(UID).orElse(null);

            if (parameter == null) {
                response.setStatus(-200);
                response.setMessage("Parameter not found.");
                response.setMessageDetail("Parameter not found.");
                return response;
            }

            parameter = (ParameterEntity) mergeObjects(parameter,dto);
            setBaseColumnsForUpdate(parameter,"001");
            parameter = parameterRepository.save(parameter);
            response.setData(mergeObjects(dto,parameter));
        }catch (Exception e) {
            response.setStatus(-200);
            response.setMessage("Error creating parameter.");
            response.setMessageDetail(e.getMessage());
        }
        return response;
    }

    public GenericResultResponse getParametersByType(String type) {
        GenericResultResponse response = new GenericResultResponse();
        try {
            List<ParameterEntity> parameters = parameterRepository.findAllByType(type).orElse(null);

            if (parameters == null) {
                response.setStatus(-200);
                response.setMessage("no parameters of this type found.");
                response.setMessageDetail("no parameters of this type found.");
                return response;
            }

            response.setData(parameters.stream()
                    .map(parameter -> (ParameterDTO) mergeObjects(new ParameterDTO(), parameter))
                    .collect(Collectors.toList()));

        }catch (Exception e) {
            response.setStatus(-200);
            response.setMessage("Error creating parameter.");
            response.setMessageDetail(e.getMessage());
        }
        return response;
    }

    public GenericResultResponse getParametersByTypeFromRedis(String type) {
        GenericResultResponse response = new GenericResultResponse();
        try {
            Map<String, ParameterEntity> objectsMap = hashOperations.entries(KEY_PREFIX + type);

            response.setData(objectsMap.values().stream()
                    .map(parameter -> (ParameterDTO) mergeObjects(new ParameterDTO(), parameter))
                    .collect(Collectors.toList()));

        }catch (Exception e) {
            response.setStatus(-200);
            response.setMessage("Error creating parameter.");
            response.setMessageDetail(e.getMessage());
        }
        return response;
    }

    public GenericResultResponse getParameterByUID(String UID) {
        GenericResultResponse response = new GenericResultResponse();
        try{
            ParameterEntity parameter = parameterRepository.findById(UID).orElse(null);

            if (parameter == null) {
                response.setStatus(-200);
                response.setMessage("Parameter not found.");
                response.setMessageDetail("Parameter not found.");
                return response;
            }

            response.setData(mergeObjects(new ParameterDTO(),parameter));

        }catch (Exception e) {
            response.setStatus(-200);
            response.setMessage("Error creating parameter.");
            response.setMessageDetail(e.getMessage());
        }
        return response;
    }

    public GenericResultResponse getParameterByUIDFromRedis(String UID) {
        GenericResultResponse response = new GenericResultResponse();
        try{
            Set<String> keys = redisTemplate.keys(KEY_PREFIX + "*");

            ParameterEntity parameter = null;
            if (keys != null) {
                for (String key : keys) {
                    // Her bir Hash'te uid'yi ara
                    parameter = hashOperations.get(key, UID);
                    if (parameter != null) {
                        break;
                    }
                }
            }

            if(parameter == null) {
                response.setStatus(-200);
                response.setMessage("Parameter not found.");
                response.setMessageDetail("Parameter not found.");
                return response;
            }

            response.setData(mergeObjects(new ParameterDTO(),parameter));

        }catch (Exception e) {
            response.setStatus(-200);
            response.setMessage("Error creating parameter.");
            response.setMessageDetail(e.getMessage());
        }
        return response;
    }

    public GenericResultResponse getAllParameters() {
        GenericResultResponse response = new GenericResultResponse();
        try{
            List<ParameterEntity> parameters = parameterRepository.findAll();

            response.setData(parameters.stream()
                    .map(parameter -> (ParameterDTO) mergeObjects(new ParameterDTO(), parameter))
                    .collect(Collectors.toList()));

        }catch (Exception e) {
            response.setStatus(-200);
            response.setMessage("Error creating parameter.");
            response.setMessageDetail(e.getMessage());
        }
        return response;
    }

    public GenericResultResponse getAllParametersFromRedis() {
        GenericResultResponse response = new GenericResultResponse();
        try{

            Set<String> keys = redisTemplate.keys(KEY_PREFIX + "*");
            List<ParameterEntity> allObjects = new ArrayList<>();

            if (keys != null) {
                for (String key : keys) {
                    Map<String, ParameterEntity> objectsMap = hashOperations.entries(key);
                    allObjects.addAll(objectsMap.values()); // Her bir type'ın nesnelerini listeye ekle
                }
            }


            response.setData(allObjects.stream()
                    .map(parameter -> (ParameterDTO) mergeObjects(new ParameterDTO(), parameter))
                    .collect(Collectors.toList()));

        }catch (Exception e) {
            response.setStatus(-200);
            response.setMessage("Error creating parameter.");
            response.setMessageDetail(e.getMessage());
        }
        return response;
    }

    public GenericResultResponse deleteParameterByUID(String UID) {
        GenericResultResponse response = new GenericResultResponse();
        try{
            ParameterEntity parameter = parameterRepository.findById(UID).orElse(null);

            if (parameter == null) {
                response.setStatus(-200);
                response.setMessage("Parameter not found.");
                response.setMessageDetail("Parameter not found.");
                return response;
            }

            parameterRepository.delete(parameter);
            response.setData("success");
        }catch (Exception e) {
            response.setStatus(-200);
            response.setMessage("Error creating parameter.");
            response.setMessageDetail(e.getMessage());
        }
        return response;
    }
}
