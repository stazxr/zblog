/*
 * Copyright 2020-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.stazxr.zblog.base.security.jwt.encoder;

import org.springframework.security.oauth2.core.converter.ClaimConversionService;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithm;
import org.springframework.util.Assert;

import java.net.URL;
import java.util.*;
import java.util.function.Consumer;

/**
 * The JOSE header is a JSON object representing the header parameters of a JSON Web Token,
 * whether the JWT is a JWS or JWE, that describe the cryptographic operations applied to the JWT
 * and optionally, additional properties of the JWT.
 *
 * @author Anoop Garlapati
 * @author Joe Grandja
 */
@SuppressWarnings("ALL")
public final class JoseHeader {
    private final Map<String, Object> headers;

    private JoseHeader(Map<String, Object> headers) {
        this.headers = Collections.unmodifiableMap(new HashMap<>(headers));
    }

    public JwsAlgorithm getJwsAlgorithm() {
        return getHeader(JoseHeaderNames.ALG);
    }

    public URL getJwkSetUri() {
        return getHeader(JoseHeaderNames.JKU);
    }

    public Map<String, Object> getJwk() {
        return getHeader(JoseHeaderNames.JWK);
    }

    public String getKeyId() {
        return getHeader(JoseHeaderNames.KID);
    }

    public URL getX509Uri() {
        return getHeader(JoseHeaderNames.X5U);
    }

    public List<String> getX509CertificateChain() {
        return getHeader(JoseHeaderNames.X5C);
    }

    public String getX509SHA1Thumbprint() {
        return getHeader(JoseHeaderNames.X5T);
    }

    public String getX509SHA256Thumbprint() {
        return getHeader(JoseHeaderNames.X5T_S256);
    }

    public Set<String> getCritical() {
        return getHeader(JoseHeaderNames.CRIT);
    }

    public String getType() {
        return getHeader(JoseHeaderNames.TYP);
    }

    public String getContentType() {
        return getHeader(JoseHeaderNames.CTY);
    }

    public Map<String, Object> getHeaders() {
        return this.headers;
    }

    @SuppressWarnings("unchecked")
    public <T> T getHeader(String name) {
        Assert.hasText(name, "name cannot be empty");
        return (T) getHeaders().get(name);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder withAlgorithm(JwsAlgorithm jwsAlgorithm) {
        return new Builder(jwsAlgorithm);
    }

    public static Builder from(JoseHeader headers) {
        return new Builder(headers);
    }

    public static final class Builder {
        private final Map<String, Object> headers = new HashMap<>();

        private Builder() {
        }

        private Builder(JwsAlgorithm jwsAlgorithm) {
            Assert.notNull(jwsAlgorithm, "jwsAlgorithm cannot be null");
            header(JoseHeaderNames.ALG, jwsAlgorithm);
        }

        private Builder(JoseHeader headers) {
            Assert.notNull(headers, "headers cannot be null");
            this.headers.putAll(headers.getHeaders());
        }

        public Builder jwsAlgorithm(JwsAlgorithm jwsAlgorithm) {
            return header(JoseHeaderNames.ALG, jwsAlgorithm);
        }

        public Builder jwkSetUri(String jwkSetUri) {
            return header(JoseHeaderNames.JKU, jwkSetUri);
        }

        public Builder jwk(Map<String, Object> jwk) {
            return header(JoseHeaderNames.JWK, jwk);
        }

        public Builder keyId(String keyId) {
            return header(JoseHeaderNames.KID, keyId);
        }

        public Builder x509Uri(String x509Uri) {
            return header(JoseHeaderNames.X5U, x509Uri);
        }

        public Builder x509CertificateChain(List<String> x509CertificateChain) {
            return header(JoseHeaderNames.X5C, x509CertificateChain);
        }

        public Builder x509SHA1Thumbprint(String x509SHA1Thumbprint) {
            return header(JoseHeaderNames.X5T, x509SHA1Thumbprint);
        }

        public Builder x509SHA256Thumbprint(String x509SHA256Thumbprint) {
            return header(JoseHeaderNames.X5T_S256, x509SHA256Thumbprint);
        }

        public Builder critical(Set<String> headerNames) {
            return header(JoseHeaderNames.CRIT, headerNames);
        }

        public Builder type(String type) {
            return header(JoseHeaderNames.TYP, type);
        }

        public Builder contentType(String contentType) {
            return header(JoseHeaderNames.CTY, contentType);
        }

        public Builder header(String name, Object value) {
            Assert.hasText(name, "name cannot be empty");
            Assert.notNull(value, "value cannot be null");
            this.headers.put(name, value);
            return this;
        }

        public Builder headers(Consumer<Map<String, Object>> headersConsumer) {
            headersConsumer.accept(this.headers);
            return this;
        }

        public JoseHeader build() {
            Assert.notEmpty(this.headers, "headers cannot be empty");
            convertAsUrl(JoseHeaderNames.JKU);
            convertAsUrl(JoseHeaderNames.X5U);
            return new JoseHeader(this.headers);
        }

        private void convertAsUrl(String header) {
            Object value = this.headers.get(header);
            if (value != null) {
                URL convertedValue = ClaimConversionService.getSharedInstance().convert(value, URL.class);
                Assert.isTrue(convertedValue != null,
                        () -> "Unable to convert header '" + header + "' of type '" + value.getClass() + "' to URL.");
                this.headers.put(header, convertedValue);
            }
        }
    }
}