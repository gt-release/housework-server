package jp.co.takawagu.houseworkserver.model

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.YearMonth

data class PocketRequest(
        val userName: String,
        @JsonFormat(pattern="yyyy/MM")
        val selectedMonth: YearMonth)