package top.pi1grim.ea.common.utils;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

public class JWTUtils {
    private JWTUtils() {

    }

    public static final Key KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public static final String CLAIM_KEY = "username";

    public static String genToken(String username) {
        return Jwts.builder()
                .claim(CLAIM_KEY, username)
                .setIssuer("PILGRIM")
                .setSubject("LOGIN KEY")
                .signWith(KEY)
                .compact();
    }

    public static String parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get(CLAIM_KEY)
                .toString();
    }
}
