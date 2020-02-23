package jp.co.takawagu.houseworkserver.model

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.data.annotation.Id
import java.time.LocalDateTime
import java.util.*

data class Memo(
        @Id
        val memoId: UUID?,
        val userId: UUID,
        val contents: String,
        @JsonFormat(pattern="yyyy/MM/dd HH:mm:ss")
        val insertTime: LocalDateTime?,
        @JsonFormat(pattern="yyyy/MM/dd HH:mm:ss")
        val updateTime: LocalDateTime?)