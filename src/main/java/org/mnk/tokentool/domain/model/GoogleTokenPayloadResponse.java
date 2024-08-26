package org.mnk.tokentool.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleTokenPayloadResponse {

    private String iss;
    private Long nbf;
    private String aud;
    private String sub;
    private String email;

    @JsonProperty("email_verified")
    private boolean emailVerified;
    private String azp;
    private String name;
    private String picture;

    @JsonProperty("given_name")
    private String givenName;

    @JsonProperty("family_name")
    private String familyName;
    private Long iat;
    private Long exp;
    private String jti;
}
