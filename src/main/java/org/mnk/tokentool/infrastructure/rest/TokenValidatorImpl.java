package org.mnk.tokentool.infrastructure.rest;

import com.github.benmanes.caffeine.cache.AsyncCache;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.mnk.tokentool.domain.common.TokenTools;
import org.mnk.tokentool.domain.model.CacheableUserInfo;
import org.mnk.tokentool.domain.model.GoogleTokenPayloadResponse;
import org.mnk.tokentool.domain.model.UsedEnv;
import org.mnk.tokentool.domain.service.TokenValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class TokenValidatorImpl implements TokenValidator {

    private static final String VALIDATION_TOKEN_PATH = "gcu/verifier-token";

    @Value("${gcu.url}")
    private String gcuUrl;

    @Value("${gcu.localUrl}")
    private String gcuLocalUrl;

    @Value("${gcu.port}")
    private String gcuPort;

    private final AsyncCache<String, Object> cache;

    private final RestTemplate restTemplate;

    @Override
    public boolean isTokenRequired(String produit) {
        return false;
    }

    @SneakyThrows
    @Override
    public CacheableUserInfo getUserInfo(String token, String produit, UsedEnv usedEnv) {
        final String serverUrl = UsedEnv.SERVER_USE.equals(usedEnv) ? gcuUrl : gcuLocalUrl;
        final String fullUrl = serverUrl + ":" + gcuPort + "/" + VALIDATION_TOKEN_PATH + "?produit=" + produit;
        if (TokenTools.isValidToken(token)) {
            String idUser = "";
            final GoogleTokenPayloadResponse tokenInfo = TokenTools.getTokenInfo(token);
            final String key = Objects.requireNonNull(tokenInfo).getEmail() + tokenInfo.getExp();
            var result = findByIdIfPresent(key);

            if (Objects.nonNull(result)) {
                return (CacheableUserInfo) result.get();
            }

            var headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + token);
            ResponseEntity<Object> response = restTemplate.exchange(fullUrl, HttpMethod.GET, new HttpEntity<>(headers), Object.class);
            idUser = getIdUser(response);
            var cacheableUserInfo = CacheableUserInfo.builder()
                    .id(key)
                    .email(tokenInfo.getEmail())
                    .principalUserEmail(idUser)
                    .build();
            CompletableFuture<Object> future = CompletableFuture.completedFuture(cacheableUserInfo);
            createOrUpdateCache(key, future);
            return cacheableUserInfo;
        }
        throw new Exception("Invalid token");
    }

    private String getIdUser(final ResponseEntity<Object> response) {
        Map<String, Object> result = (Map<String, Object>) response.getBody();
        Map<String, Object> utilisateur = (Map<String, Object>) result.get("utilisateur");
        return (String) utilisateur.get("emailUtilisateurPrincipal");
    }

    private CompletableFuture<Object> findByIdIfPresent(final String id) {
        return cache.getIfPresent(id);
    }

    private void createOrUpdateCache(final String id, final CompletableFuture<Object> obj) {
        cache.put(id, obj);
    }

    private void deleteCache(final String id) {
        cache.synchronous().invalidate(id);
    }
}
