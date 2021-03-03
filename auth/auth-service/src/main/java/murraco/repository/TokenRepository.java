package murraco.repository;

public interface TokenRepository {

    String getTokenByKey(String key);

    void saveToken(String key, String token);
}
