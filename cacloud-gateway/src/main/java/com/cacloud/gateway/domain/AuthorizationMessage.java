package com.cacloud.gateway.domain;

import lombok.Data;

@Data
public class AuthorizationMessage {
    private String protocol;
    private String ak;
    private String credentialScope;
    private String signedHeaders;
    private String signature;
}
