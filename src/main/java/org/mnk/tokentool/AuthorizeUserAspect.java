package org.mnk.tokentool;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.mnk.tokentool.domain.model.CacheableUserInfo;
import org.mnk.tokentool.domain.service.TokenValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;

@Aspect
@Component
@RequiredArgsConstructor
public class AuthorizeUserAspect {

    @Value("${produit}")
    private String produit;

    private static final String ID_USER = "id-user";
    private static final String ID_HOST = "id-host";
    private static final String ID_GCU = "id-gcu";
    private static final String AUTHORIZATION = "authorization";

    private final TokenValidator tokenValidator;

    @Around("@annotation(org.mnk.tokentool.AuthorizeUser)")
    public Object excuteAspect(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();

        for (Object arg : args) {
            if (arg instanceof Map<?, ?> map) {
                @SuppressWarnings("unchecked")
                Map<String, String> headers = (Map<String, String>) map;

                clearUserId(headers);

                String token = headers.get(AUTHORIZATION);
                if (!StringUtils.hasText(token)) {
                    throw new Exception("Authorization header is empty!");
                }

                CacheableUserInfo v = tokenValidator.getUserInfo(token, produit);
                headers.put(ID_USER, v.getEmail());
                headers.put(ID_HOST, v.getPrincipalUserEmail());
                headers.put(ID_GCU, v.getId());
                break;
            }
        }

        return joinPoint.proceed(args);
    }

    private void clearUserId(final Map<String, String> headers) {
        headers.remove(ID_USER);
        headers.remove(ID_HOST);
        headers.remove(ID_GCU);
    }
}
