package com.zerotb.zerotb.data.responses.auth

import com.zerotb.zerotb.data.responses.data.User

data class LoginResponse(
    val token: String,
    val user: User
)

data class UserResponse(
    val user: User
)

data class RegisterResponse(
    val message: String
)

data class ForgotResponse(
    val message: String
)

data class LogoutResponse(
    val message: String
)

data class ProfileResponse(
    val message: String
)