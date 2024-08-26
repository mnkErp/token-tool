package org.mnk.tokentool.domain.service;


import org.mnk.tokentool.domain.model.GoogleTokenPayloadResponse;
import java.util.Base64;
public class TokenManager {


    public static GoogleTokenPayloadResponse readToken(final String token) {
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        return payloadResponseToGoogleModel(new String(decoder.decode(chunks[1])));
    }

    private static GoogleTokenPayloadResponse payloadResponseToGoogleModel(final String json) {

        return LocalObjectMapper.convertObject(json, GoogleTokenPayloadResponse.class);
    }
}
