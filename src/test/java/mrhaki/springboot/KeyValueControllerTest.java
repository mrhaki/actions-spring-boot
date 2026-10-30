package mrhaki.springboot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(KeyValueController.class)
class KeyValueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private KeyValueService keyValueService;

    @Test
    void addEndpointInvokesComponentAndReturnsData() throws Exception {
        mockMvc.perform(post("/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "key": "app.env",
                                  "value": "production"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.key").value("app.env"))
                .andExpect(jsonPath("$.value").value("production"));

        verify(keyValueService).save(new KeyValue("app.env", "production"));
    }
}
