package com.shail.security.helper;

import com.shail.security.model.LoginResponse;
import com.shail.security.util.SecretKeyUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.java.Log;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class TokenHelper {

    @Value("${security.auth.header.param.name}")
    private String AUTH_HEADER_PARAM_NAME;

    @Value("${security.auth.header.token.prefix}")
    private String AUTH_HEADER_TOKEN_PREFIX;

    @Value("${security.auth.access.expire.in}")
    private Long ACCESS_EXPIRE_IN;

    @Value("${security.auth.header.token.username}")
    private String AUTH_HEADER_USERNAME;

    @Value("${security.auth.header.token.password}")
    private String AUTH_HEADER_PASSWORD;

    @Value("${security.auth.header.token.roles}")
    private String AUTH_HEADER_ROLES;

    @Value("${security.auth.header.token.kid}")
    private String AUTH_HEADER_KID;

    @Value("${security.auth.refresh.expire.in}")
    private Long REFRESH_EXPIRE_IN;

    @Autowired
    private SecretKeyUtil secretKeyUtil;

    public String getToken(HttpServletRequest request) {
        String token = request.getHeader(AUTH_HEADER_PARAM_NAME);
        return token != null ? token.substring(AUTH_HEADER_TOKEN_PREFIX.length()) : null;
    }

    public Integer getKid(HttpServletRequest request) {
        return request.getHeader(AUTH_HEADER_KID) != null ? request.getIntHeader(AUTH_HEADER_KID) : -1;
    }

    public String getTokenScope(String token, Integer kid) throws Exception{
        final Claims claims = getJwtClaims(token, kid);
        return claims != null ? (String) claims.get("scope") : null;
    }

    public String getUsername(String token, Integer kid) throws Exception{
        final Claims claims = getJwtClaims(token, kid);
        return claims != null ? (String) claims.get("username") : null;
    }

    public boolean isValidToken(String token, Integer kid) throws Exception{
        final Claims claims = getJwtClaims(token, kid);
        return !(claims.getExpiration().before(new Date()));
    }

    public LoginResponse generateToken(String username, String scope, Collection<GrantedAuthority> authorities, LoginResponse response) throws NoSuchAlgorithmException {
        int kid = SecureRandom.getInstanceStrong().nextInt(11);
        String SIGNING_KEY = SecretKeyUtil.getKeyList().get(kid);

        Claims claims = Jwts.claims();
        claims.put(AUTH_HEADER_USERNAME, username);
        String roles = (authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")));
        claims.put(AUTH_HEADER_ROLES, roles);
        claims.put("scope", scope);

        LocalDateTime currentTime = LocalDateTime.now();

        String token = Jwts.builder()
                .setClaims(claims)
                .setId(UUID.randomUUID().toString())
                .setIssuer("blog.shailendra.dev")
                .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(scope.contains("ACCESS") ? Date.from(currentTime.plusMinutes(ACCESS_EXPIRE_IN).atZone(ZoneId.systemDefault()).toInstant()) : Date.from(currentTime.plusMinutes(REFRESH_EXPIRE_IN).atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, SIGNING_KEY.getBytes())
                .compact();
        if(response != null) {
            if(scope.contains("ACCESS")) {
                response.setAccessToken(token);
                response.setAccessKid(kid);
            }
            else {
                response.setRefreshToken(token);
                response.setRefreshKid(kid);
            }
        }

        return response;
    }
    private Claims getJwtClaims(String token, Integer kid) throws Exception{
        try {
            final Claims claims = Jwts.parser().setSigningKey(SecretKeyUtil.getKeyList().get(kid).getBytes())
                    .parseClaimsJws(token).getBody();
            return claims;
        }
        catch (Exception e) {
            throw new Exception("INVALID_JWT_TOKEN");
        }
    }

}
