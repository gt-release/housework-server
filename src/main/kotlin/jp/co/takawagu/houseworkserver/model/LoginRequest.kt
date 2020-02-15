package jp.co.takawagu.houseworkserver.model

data class LoginRequest(
        val userName: String,
        val password: String
)