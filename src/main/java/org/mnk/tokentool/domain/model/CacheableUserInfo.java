package org.mnk.tokentool.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CacheableUserInfo {

    private String id;
    private String principalUserEmail;
    private String email;
}
