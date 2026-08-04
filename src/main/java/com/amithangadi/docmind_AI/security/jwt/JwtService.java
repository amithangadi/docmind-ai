package com.amithangadi.docmind_AI.security.jwt;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.expiration}")
	private long jwtExpiration;
	
//	Generate jwt token
	
	public String generateToken(String email)
	{
		return Jwts.builder()
				.subject(email)
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + jwtExpiration))
				.signWith(getSigningKey())
				.compact();
	}
	
//	extract email
	
	public String extractUsername(String token)
	{
		return extractAllClaims(token).getSubject();
	}
	
//	Validate Token
	
	public boolean  isTokenValid(String token, String email)
	{
		String username = extractUsername(token);
		
		return username.equals(email)
				&& !isTokenExpired(token);
	}
	
//	Check expiry
	
	private boolean isTokenExpired(String token)
	{
		return extractAllClaims(token)
				.getExpiration()
				.before(new Date());
	}
	
//	Reading claims
	
	private Claims extractAllClaims(String token)
	{
		
		return Jwts.parser()
				.verifyWith(getSigningKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}
	
//	Secret Key
	
	private SecretKey getSigningKey()
	{
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
