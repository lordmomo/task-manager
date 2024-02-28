package com.momo.task.manager.dto;

import com.momo.task.manager.utils.AuthStatus;

public record AuthResponseDto (String accessToken, String refreshToken, AuthStatus authStatus){
}
