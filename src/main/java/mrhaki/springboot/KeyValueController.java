package mrhaki.springboot;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KeyValueController {

    private final KeyValueService keyValueService;

    public KeyValueController(KeyValueService keyValueService) {
        this.keyValueService = keyValueService;
    }

    @PostMapping("/add")
    public ResponseEntity<KeyValue> add(@RequestBody KeyValue keyValue) {
        keyValueService.save(keyValue);
        return ResponseEntity.ok(keyValue);
    }
}
