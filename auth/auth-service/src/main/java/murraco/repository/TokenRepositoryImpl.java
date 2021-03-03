package murraco.repository;

import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class TokenRepositoryImpl implements TokenRepository {

    private HashMap<String, String> storage = new HashMap<>();

    @Override
    public String getTokenByKey(String key) {
        return storage.get(key);
    }

    @Override
    public void saveToken(String key, String token) {
        storage.put(key, token);
    }
}
