package net.okyildiz.parametre.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.okyildiz.parametre.utils.BaseService;
import net.okyildiz.parametre.utils.GenericResultResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParameterService extends BaseService {

    private final ParameterRepository parameterRepository;
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;
    private static final String REDIS_KEY = "parameters";

    public ParameterService(ParameterRepository parameterRepository, ObjectMapper objectMapper, RedisTemplate<String, String> redisTemplate) {
        this.parameterRepository = parameterRepository;
        this.objectMapper = objectMapper;
        this.redisTemplate = redisTemplate;
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

            /*
            List<ParameterDTO> parametersDTO = new ArrayList<>();
            for (ParameterEntity parameter : parameters) {
                ParameterDTO dto = new ParameterDTO();
                dto = (ParameterDTO) mergeObjects(dto,parameter);
                parametersDTO.add(dto);
            }
            */

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

            List<ParameterEntity> parameters = new ArrayList<>();
            ObjectMapper objectMapper = new ObjectMapper();

            for (String key : redisTemplate.keys(REDIS_KEY)) {
                String jsonValue = redisTemplate.opsForValue().get(key);
                if (jsonValue != null) {
                    try {
                        ParameterEntity parameter = objectMapper.readValue(jsonValue, ParameterEntity.class);
                        parameters.add(parameter);
                    } catch (IOException e) {
                        e.printStackTrace();
                        // Hata yönetimi
                    }
                }
            }

            response.setData(parameters);

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
