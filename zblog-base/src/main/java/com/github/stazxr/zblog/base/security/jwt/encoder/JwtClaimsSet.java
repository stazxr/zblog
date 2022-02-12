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

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimAccessor;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.util.Assert;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * The {@link Jwt JWT} Claims Set is a JSON object representing the claims conveyed by a JSON Web Token.
 *
 * @author Anoop Garlapati
 * @author Joe Grandja
 */
public final class JwtClaimsSet implements JwtClaimAccessor {
	private final Map<String, Object> claims;

	private JwtClaimsSet(Map<String, Object> claims) {
		this.claims = Collections.unmodifiableMap(new HashMap<>(claims));
	}

	@Override
	public Map<String, Object> getClaims() {
		return this.claims;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static Builder from(JwtClaimsSet claims) {
		return new Builder(claims);
	}

	public static final class Builder {
		private final Map<String, Object> claims = new HashMap<>();

		private Builder() {
		}

		private Builder(JwtClaimsSet claims) {
			Assert.notNull(claims, "claims cannot be null");
			this.claims.putAll(claims.getClaims());
		}

		public Builder issuer(String issuer) {
			return claim(JwtClaimNames.ISS, issuer);
		}

		public Builder subject(String subject) {
			return claim(JwtClaimNames.SUB, subject);
		}

		public Builder audience(List<String> audience) {
			return claim(JwtClaimNames.AUD, audience);
		}

		public Builder expiresAt(Instant expiresAt) {
			return claim(JwtClaimNames.EXP, expiresAt);
		}

		public Builder notBefore(Instant notBefore) {
			return claim(JwtClaimNames.NBF, notBefore);
		}

		public Builder issuedAt(Instant issuedAt) {
			return claim(JwtClaimNames.IAT, issuedAt);
		}

		public Builder id(String jti) {
			return claim(JwtClaimNames.JTI, jti);
		}

		public Builder claim(String name, Object value) {
			Assert.hasText(name, "name cannot be empty");
			Assert.notNull(value, "value cannot be null");
			this.claims.put(name, value);
			return this;
		}

		public Builder claims(Consumer<Map<String, Object>> claimsConsumer) {
			claimsConsumer.accept(this.claims);
			return this;
		}

		public JwtClaimsSet build() {
			Assert.notEmpty(this.claims, "claims cannot be empty");
			return new JwtClaimsSet(this.claims);
		}
	}
}