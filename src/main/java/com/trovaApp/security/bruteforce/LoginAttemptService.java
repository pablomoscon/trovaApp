package com.trovaApp.security.bruteforce;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptService {

    private static final int MAX_ATTEMPTS = 5;
    private static final long BLOCK_TIME_MS = TimeUnit.MINUTES.toMillis(15); // 15 minutos

    private static class AttemptInfo {
        int attempts;
        long lastAttemptTime;
    }

    private final ConcurrentHashMap<String, AttemptInfo> attemptsCache = new ConcurrentHashMap<>();

    private String key(String username, String ip) {
        return username + ":" + ip;
    }

    public void loginFailed(String username, String ip) {
        String key = key(username, ip);
        AttemptInfo info = attemptsCache.getOrDefault(key, new AttemptInfo());
        info.attempts++;
        info.lastAttemptTime = System.currentTimeMillis();
        attemptsCache.put(key, info);
    }

    public void loginSucceeded(String username, String ip) {
        String key = key(username, ip);
        attemptsCache.remove(key);
    }

    public boolean isBlocked(String username, String ip) {
        String key = key(username, ip);
        AttemptInfo info = attemptsCache.get(key);
        if (info == null) return false;

        if (info.attempts >= MAX_ATTEMPTS) {
            long elapsed = System.currentTimeMillis() - info.lastAttemptTime;
            if (elapsed < BLOCK_TIME_MS) {
                return true;
            } else {
                // Tiempo de bloqueo expirado
                attemptsCache.remove(key);
                return false;
            }
        }
        return false;
    }
}
