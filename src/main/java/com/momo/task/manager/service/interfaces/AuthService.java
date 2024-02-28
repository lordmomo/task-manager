package com.momo.task.manager.service.interfaces;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public interface AuthService {
    String login(String username, String password);

    Optional<String> getTokenFromRequest(HttpServletRequest request);
}
