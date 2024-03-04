package com.momo.task.manager.response;

import com.momo.task.manager.utils.AuthStatus;

public record AuthResponseDto (String accessToken, String refreshToken, AuthStatus authStatus){
}
