package jp.co.takawagu.houseworkserver.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table("user_info")
data class User(
        @Id
        val userId: UUID?,
        val userName: String,
        var password: String?,
        val color: String
)