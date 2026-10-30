package mrhaki.springboot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class KeyValueIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private KeyValueService keyValueService;

    @Autowired
    private JdbcClient jdbcClient;

    @Test
    void addEndpointSavesDataToDatabaseViaSpringJdbc() throws Exception {
        String json = """
                {
                  "key": "feature.enabled",
                  "value": "true"
                }
                """;

        mockMvc.perform(post("/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.key").value("feature.enabled"))
                .andExpect(jsonPath("$.value").value("true"));

        // Verify data was persisted in database via KeyValueService
        assertThat(keyValueService.findByKey("feature.enabled"))
                .isPresent()
                .hasValue(new KeyValue("feature.enabled", "true"));

        // Verify directly in database table via Spring JDBC
        String valueFromDb = jdbcClient.sql("SELECT value FROM key_value WHERE key = :key")
                .param("key", "feature.enabled")
                .query(String.class)
                .single();

        assertThat(valueFromDb).isEqualTo("true");
    }
}
