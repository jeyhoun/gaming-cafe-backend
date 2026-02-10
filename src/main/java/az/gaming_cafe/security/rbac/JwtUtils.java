package az.gaming_cafe.security.rbac;

import az.gaming_cafe.model.entity.RoleEntity;
import az.gaming_cafe.model.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${jwt.access-token.expiration:900000}") // 15 minutes
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token.expiration:604800000}")// 7 days
    private long refreshTokenExpiration;

    private final SecretKey key;

    public static class TokenWithJti {
        private final String refreshToken;
        private final String jti;

        public TokenWithJti(String token, String claimJti) {
            this.refreshToken = token;
            this.jti = claimJti;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public String getJti() {
            return jti;
        }
    }

    public JwtUtils(@Value("${jwt.secret-key}") String secretKey) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public TokenWithJti generateRefreshToken(String username) {
        String jti = UUID.randomUUID().toString();

        Map<String, Object> claims = new HashMap<>();
        claims.put("tokenType", "refresh");
        claims.put(CLAIM_JTI, jti);

        String token = generateToken(username, claims, refreshTokenExpiration);

        return new TokenWithJti(token, jti);
    }

    public String generateAccessToken(UserEntity user) {
        Map<String, Object> claims = buildClaims(user);
        return generateToken(user.getUsername(), claims, accessTokenExpiration);
    }

    public String generateToken(String username, Map<String, Object> extraClaims, long expiration) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(username)
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

    public boolean isTokenValid(String token, String username) {
        try {
            final String extractedUsername = extractUsername(token);
            return extractedUsername.equals(username) && !isTokenExpired(token);
            //CHECKSTYLE:OFF
        } catch (Exception e) {
            return false;
        }  //CHECKSTYLE:ON
    }

    public String extractJti(String token) {
        return extractAllClaims(token).get(CLAIM_JTI, String.class);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
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
