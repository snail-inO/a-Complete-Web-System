package com.multi.common.utils;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JwtUtil {
    /*过期时间 24hr*/
    private static final int EXPIRATION_TIME = 60 * 60 * 24;
    /*密钥*/
    private static final String SECRET = "Oz[;|Ona29h(_%$NLfnvra[=AVMma132]0svfajpNFN~FAS;MFKNqijfs;avn";
    /*前缀*/
    public static final String TOKEN_PREFIX = "Bearer ";
    /*表头授权*/
    public static final String AUTHORIZATION = "Authorization";

    public static String generateToken(String userId) {
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();

        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND, EXPIRATION_TIME);
        Date time = calendar.getTime();
        Map<String, Object> map = new HashMap<>();
        log.info("now: " + now);
        log.info("time: " + time);
        map.put("userId", userId);
        String jwt = Jwts.builder()
                .setClaims(map)
                .setIssuedAt(now)
                .setExpiration(time)
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
        return TOKEN_PREFIX + jwt;
    }

    public static String validateToken(String token) {
        try {
            Map<String, Object> body = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody();
            String userId = body.get("userId").toString();
            return userId;
        } catch (ExpiredJwtException e) {
            throw e;
        } catch (UnsupportedJwtException e) {
            throw e;
        } catch (MalformedJwtException e) {
            throw e;
        } catch (SignatureException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }
}
