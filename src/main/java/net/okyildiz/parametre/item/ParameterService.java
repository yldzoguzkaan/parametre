package net.okyildiz.parametre.item;

import net.okyildiz.parametre.utils.BaseService;
import net.okyildiz.parametre.utils.GenericResultResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParameterService extends BaseService {

    private final ParameterRepository parameterRepository;

    public ParameterService(ParameterRepository parameterRepository) {
        this.parameterRepository = parameterRepository;
    }

    @CacheEvict(value = {"parametersByType", "parameterByUID", "allParameters"}, allEntries = true)
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

    @CacheEvict(value = {"parametersByType", "parameterByUID", "allParameters"}, allEntries = true)
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

    @Cacheable(value = "parametersByType", key = "#type")
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

    @Cacheable(value = "parameterByUID", key = "#UID")
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

    @Cacheable(value = "allParameters")
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

    @CacheEvict(value = {"parametersByType", "parameterByUID", "allParameters"}, allEntries = true)
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
