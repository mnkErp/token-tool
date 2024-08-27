package org.mnk.tokentool.domain.service;

import org.mnk.tokentool.domain.model.CacheableUserInfo;

public interface TokenValidator {

    boolean isTokenRequired(String produit);
    CacheableUserInfo getUserInfo(String token, String produit);
}
