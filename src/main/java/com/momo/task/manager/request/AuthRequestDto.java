package com.momo.task.manager.request;

public record AuthRequestDto (
        String firstName,
        String lastName,
        String email,
        String username,
        String password) {
}