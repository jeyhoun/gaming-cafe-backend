package az.gaming_cafe.security.rbac;

import az.gaming_cafe.config.JwtProperties;
import az.gaming_cafe.model.entity.RoleEntity;
import az.gaming_cafe.model.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    private static final String CLAIM_JTI = "jti";
    private static final String CLAIM_ROLES = "roles";

    private final SecretKey key;
    private final JwtProperties jwtProperties;

    public JwtUtils(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.key = Keys.hmacShaKeyFor(jwtProperties.secretKey().getBytes(StandardCharsets.UTF_8));
    }

    public record TokenWithJti(String refreshToken, String jti) {}

    public TokenWithJti generateRefreshToken(Long userId) {
        String jti = UUID.randomUUID().toString();

        Map<String, Object> claims = new HashMap<>();
        claims.put("tokenType", "refresh");
        claims.put(CLAIM_JTI, jti);

        String token = generateToken(userId, claims, jwtProperties.refreshTokenExpiration());

        return new TokenWithJti(token, jti);
    }

    public String generateAccessToken(UserEntity user) {
        return generateAccessTokenWithJti(user).refreshToken();
    }

    public TokenWithJti generateAccessTokenWithJti(UserEntity user) {
        Map<String, Object> claims = buildClaims(user);
        String jti = UUID.randomUUID().toString();
        claims.put(CLAIM_JTI, jti);
        String token = generateToken(user.getId(), claims, jwtProperties.accessTokenExpiration());
        return new TokenWithJti(token, jti);
    }

    public String generateToken(Long userId, Map<String, Object> extraClaims, long expiration) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(String.valueOf(userId))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    public List<String> extractRoles(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        Object roles = claims.get(CLAIM_ROLES);
        if (roles instanceof List<?> list) {
            return list.stream()
                    .filter(Objects::nonNull)
                    .map(Object::toString)
                    .collect(Collectors.toList());
        } else if (roles != null) {
            return List.of(roles.toString());
        }
        return List.of();
    }

    public boolean isTokenValid(String token, Long userId) {
        try {
            final Long extractedUserId = extractUserId(token);
            return extractedUserId.equals(userId) && !isTokenExpired(token);
            //CHECKSTYLE:OFF
        } catch (Exception e) {
            return false;
        }  //CHECKSTYLE:ON
    }

    public String extractJti(String token) {
        return extractAllClaims(token).get(CLAIM_JTI, String.class);
    }

    public Long extractUserId(String token) {
        return Long.valueOf(extractClaim(token, Claims::getSubject));
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claimsResolver.apply(claims);
    }

    private static Map<String, Object> buildClaims(UserEntity user) {
        Map<String, Object> claims = new HashMap<>();
        Set<String> roles = user.getRoles().stream()
                .map(RoleEntity::getName)
                .collect(Collectors.toSet());
        claims.put(CLAIM_ROLES, roles);
        claims.put("userId", user.getId());
        return claims;
    }
}
