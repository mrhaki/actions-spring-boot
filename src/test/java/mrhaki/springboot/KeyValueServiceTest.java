package mrhaki.springboot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({KeyValueService.class, TestcontainersConfiguration.class})
class KeyValueServiceTest {

    @Autowired
    private KeyValueService keyValueService;

    @Test
    void savesAndRetrievesData() {
        KeyValue item = new KeyValue("sampleKey", "sampleValue");
        keyValueService.save(item);

        assertThat(keyValueService.findByKey("sampleKey"))
                .isPresent()
                .hasValue(item);
    }
}
