package org.mnk.tokentool.domain.common;

import org.mnk.tokentool.domain.model.GoogleTokenPayloadResponse;
import org.mnk.tokentool.domain.service.TokenManager;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

public class TokenTools {

    public static boolean isValidToken(final String token) {
        final GoogleTokenPayloadResponse tokenInfo = getTokenInfo(token);
        if (Objects.isNull(tokenInfo)) {
            return false;
        }
        LocalDateTime tokenDateTime = Instant.ofEpochMilli(tokenInfo.getExp() * 1000)
                .atZone(ZoneId.systemDefault()).toLocalDateTime();
        final Date tokenDate = DateTools.localDateTimeToDate(tokenDateTime);
        return !DateTools.isBeforeToday(tokenDate);
    }

    public static GoogleTokenPayloadResponse getTokenInfo(final String token) {
        if (!StringUtils.hasText(token)) {
            return null;
        }
        return TokenManager.readToken(token);
    }
}
