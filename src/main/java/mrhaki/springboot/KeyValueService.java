package mrhaki.springboot;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class KeyValueService {

    private final JdbcClient jdbcClient;

    public KeyValueService(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public void save(KeyValue keyValue) {
        save(keyValue.key(), keyValue.value());
    }

    public void save(String key, String value) {
        jdbcClient.sql("INSERT INTO key_value (key, value) VALUES (:key, :value)")
                .param("key", key)
                .param("value", value)
                .update();
    }

    public Optional<KeyValue> findByKey(String key) {
        return jdbcClient.sql("SELECT key, value FROM key_value WHERE key = :key")
                .param("key", key)
                .query(KeyValue.class)
                .optional();
    }

    public List<KeyValue> findAll() {
        return jdbcClient.sql("SELECT key, value FROM key_value")
                .query(KeyValue.class)
                .list();
    }
}
