package jp.co.takawagu.houseworkserver.model

import com.fasterxml.jackson.annotation.JsonFormat
import java.math.BigDecimal
import java.time.LocalDate

data class Pocket(
        @JsonFormat(pattern="yyyy/MM/dd")
        val baseDate: LocalDate,

        val totalMoney: BigDecimal
)