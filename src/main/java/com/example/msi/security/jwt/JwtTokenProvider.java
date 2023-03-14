package com.example.msi.security.jwt;

import com.example.msi.security.CustomUserDetails;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {
  @Value("${jwt.secret}")
  private String JWT_SECRET;

  //thời gian hiệu lực của chuỗi jwt
  @Value("${jwt.expiration}")
  private long JWT_EXPIRATION;

  // Tạo ra jwt từ thông tin user
  public String generateToken(CustomUserDetails userDetails) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);
    // Tạo chuỗi json web token từ id của user.
    return Jwts.builder()
        .setSubject(Integer.toString(userDetails.getUser().getId()))
        .setIssuedAt(now)
        .setExpiration(expiryDate)
        .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
        .compact();
  }

  // Lấy thông tin user từ jwt
  public Long getUserIdFromJWT(String token) {
    Claims claims = Jwts.parser()
        .setSigningKey(JWT_SECRET)
        .parseClaimsJws(token)
        .getBody();

    return Long.parseLong(claims.getSubject());
  }

  public boolean validateToken(String authToken) throws Exception {
    try {
      Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
      return true;
    } catch (MalformedJwtException ex) {
      log.error("Invalid JWT token");
    } catch (ExpiredJwtException ex) {
      log.error("Expired JWT token");
    } catch (UnsupportedJwtException ex) {
      log.error("Unsupported JWT token");
    } catch (IllegalArgumentException ex) {
      log.error("JWT claims string is empty.");
    } catch (SignatureException ex) {
      throw new Exception(HttpStatus.UNAUTHORIZED.toString());
   }
    return false;
  }
}
