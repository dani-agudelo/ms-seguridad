package com.ucaldas.mssecurity.Services;

import com.ucaldas.mssecurity.Models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    @Value("${jwt.secret}") // Esta anotación se utiliza para inyectar el valor de la propiedad jwt.secret en la variable secret. de application.properties
    private String secret; // Esta es la clave secreta que se utiliza para firmar el token. Debe mantenerse segura.
    // El valor de la clave secreta se inyecta desde el archivo application.properties
    @Value("${jwt.expiration}")
    private Long expiration; // Tiempo de expiración del token en milisegundos.
    private Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

  public String generateToken(User theUser) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + expiration);
    Map<String, Object> claims = new HashMap<>();
    claims.put("_id", theUser.get_id());
    claims.put("name", theUser.getName());
    claims.put("email", theUser.getEmail());
    claims.put("role", theUser.getRole());

    return Jwts.builder()
        .setClaims(claims)
        .setSubject(theUser.getName())
        .setIssuedAt(now)
        .setExpiration(expiryDate)
        .signWith(secretKey)
        .compact();
  }

  public boolean validateToken(String token) {
    try {
      Jws<Claims> claimsJws =
          Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);

      // Verifica la expiración del token
      Date now = new Date();
      if (claimsJws.getBody().getExpiration().before(now)) {
        return false;
      }

      return true;
    } catch (SignatureException ex) {
      // La firma del token es inválida
      return false;
    } catch (Exception e) {
      // Otra excepción
      return false;
    }
  }

  public User getUserFromToken(String token) {
    try {
      Jws<Claims> claimsJws =
          Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);

      Claims claims = claimsJws.getBody();

      User user = new User();
      user.set_id((String) claims.get("_id"));
      user.setName((String) claims.get("name"));
      user.setEmail((String) claims.get("email"));
      return user;
    } catch (Exception e) {
      // En caso de que el token sea inválido o haya expirado
      return null;
    }
  }
  
  // Obtiene la fecha de expiración de un token
  public LocalDateTime getExpiration(String token) {
    Jws<Claims> claimsJws =
        Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);

    return claimsJws
        .getBody()
        .getExpiration()
        .toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime();
  }
}
