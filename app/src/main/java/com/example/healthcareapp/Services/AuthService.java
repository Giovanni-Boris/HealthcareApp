package com.example.healthcareapp.Services;

import android.widget.Toast;

import com.example.healthcareapp.Entity.User;
import com.example.healthcareapp.Room.UserDAO;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.reactivex.Single;

public class AuthService {
    private final UserDAO userDao;
    public AuthService(UserDAO userDao) {
        this.userDao = userDao;
    }

    public Single<String> loginUserAndGetToken(String username, String password) {
        return userDao.login(username, password)
                .flatMap(user -> {
                    String token = generateToken(user.username);
                    return Single.just(token);
                });
    }
    private String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    private Key getSigningKey() {
        String secret = "V-TiJ3d_rRRpcsxJrJC2HxSDVSfZmDJLuWEdawoSH4I";
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}