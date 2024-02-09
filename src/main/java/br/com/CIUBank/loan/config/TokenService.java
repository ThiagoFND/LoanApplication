package br.com.CIUBank.loan.config;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.stream.Collectors;

import br.com.CIUBank.loan.entity.user.Person;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

@Service
public class TokenService {
	private final Algorithm algorithm;

	public TokenService(@Value("${api.security.token.secret}") String secret) {
		this.algorithm = Algorithm.HMAC256(secret);
	}

	public String generateToken(Person user) {
		try {
			String token = JWT.create().withIssuer("auth-api").withSubject(user.getLogin())
					.withExpiresAt(genExpirationDate()).withClaim("roles", user.getAuthorities().stream()
							.map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
					.sign(algorithm);
			return token;
		} catch (JWTCreationException exception) {
			throw new RuntimeException("Error while generating token", exception);
		}
	}

	public String validateToken(String token) {
		try {
			return JWT.require(algorithm).withIssuer("auth-api").build().verify(token).getSubject();
		} catch (JWTVerificationException exception) {
			throw new JWTVerificationException("Token verification failed", exception);
		}
	}

	public String getUserIdFromToken(String token) {
		try {
			return JWT.require(algorithm).withIssuer("auth-api").build().verify(token).getClaim("sub").asString();
		} catch (JWTVerificationException exception) {
			throw new JWTVerificationException("Token verification failed", exception);
		}
	}

	private Instant genExpirationDate() {
		return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
	}
}
