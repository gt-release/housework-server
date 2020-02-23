package jp.co.takawagu.houseworkserver.model

import java.util.*

data class LoginResponse(
        val token: String,
        val userId: UUID?,
        val userName: String,
        val color: String
)