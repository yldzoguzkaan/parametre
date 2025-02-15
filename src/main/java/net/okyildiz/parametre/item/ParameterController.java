package net.okyildiz.parametre.item;

import net.okyildiz.parametre.utils.GenericResultResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parameter")
public class ParameterController {

    private final ParameterService parameterService;

    public ParameterController(ParameterService parameterService) {
        this.parameterService = parameterService;
    }

    @PostMapping("/createParameter")
    public ResponseEntity<GenericResultResponse> createParameter(@RequestBody ParameterDTO dto) {
        return ResponseEntity.ok(parameterService.createParameter(dto));
    }

    @PutMapping("/updateParameter/{UID}")
    public ResponseEntity<GenericResultResponse> createParameter(@PathVariable String UID,@RequestBody ParameterDTO dto) {
        return ResponseEntity.ok(parameterService.updateParameter(UID,dto));
    }

    @GetMapping("/getParametersByType")
    public ResponseEntity<GenericResultResponse> getParametersByType(@RequestParam String type) {
        return ResponseEntity.ok(parameterService.getParametersByType(type));
    }

    @GetMapping("/redis/getParametersByType")
    public ResponseEntity<GenericResultResponse> getParametersByTypeFromRedis(@RequestParam String type) {
        return ResponseEntity.ok(parameterService.getParametersByTypeFromRedis(type));
    }

    @GetMapping("/getParameterByUID/{UID}")
    public ResponseEntity<GenericResultResponse> getParameterByUID(@PathVariable String UID) {
        return ResponseEntity.ok(parameterService.getParameterByUID(UID));
    }

    @GetMapping("/redis/getParameterByUID/{UID}")
    public ResponseEntity<GenericResultResponse> getParameterByUIDFromRedis(@PathVariable String UID) {
        return ResponseEntity.ok(parameterService.getParameterByUIDFromRedis(UID));
    }

    @GetMapping("/getAllParameters")
    public ResponseEntity<GenericResultResponse> getAllParameters() {
        return ResponseEntity.ok(parameterService.getAllParameters());
    }

    @GetMapping("/redis/getAllParameters")
    public ResponseEntity<GenericResultResponse> getAllParametersFromRedis() {
        return ResponseEntity.ok(parameterService.getAllParametersFromRedis());
    }

    @DeleteMapping("/deleteParameterByUID/{UID}")
    public ResponseEntity<GenericResultResponse> deleteParameterByUID(@PathVariable String UID) {
        return ResponseEntity.ok(parameterService.deleteParameterByUID(UID));
    }

}
