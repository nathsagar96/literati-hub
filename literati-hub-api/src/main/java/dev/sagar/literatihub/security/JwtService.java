package dev.sagar.literatihub.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  @Value("${application.security.jwt.expiration}")
  private Long jwtExpiration;

  @Value("${application.security.jwt.secret-key}")
  private String secretKey;

  public String generateToken(UserDetails userDetails) {
    return generateToken(new HashMap<>(), userDetails);
  }

  public String generateToken(Map<String, Object> claims, UserDetails userDetails) {
    return buildToken(claims, userDetails, jwtExpiration);
  }

  private String buildToken(
      Map<String, Object> extraClaims, UserDetails userDetails, Long jwtExpiration) {
    var authorities =
        userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

    return Jwts.builder()
        .setClaims(extraClaims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
        .claim("authorities", authorities)
        .signWith(getSignInKey())
        .compact();
  }

  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public boolean isTokenValid(String jwtToken, UserDetails userDetails) {
    final String userName = extractUsername(jwtToken);
    return (userName.equals(userDetails.getUsername())) && !isTokenExpired(jwtToken);
  }

  private boolean isTokenExpired(String jwtToken) {
    return extractExpiration(jwtToken).before(new Date());
  }

  private Date extractExpiration(String jwtToken) {
    return extractClaim(jwtToken, Claims::getExpiration);
  }

  public String extractUsername(String jwtToken) {
    return extractClaim(jwtToken, Claims::getSubject);
  }

  public <T> T extractClaim(String jwtToken, Function<Claims, T> claimResolver) {
    final Claims claims = extractAllClaims(jwtToken);
    return claimResolver.apply(claims);
  }

  private Claims extractAllClaims(String jwtToken) {
    return Jwts.parser().setSigningKey(getSignInKey()).build().parseClaimsJws(jwtToken).getBody();
  }
}
