package jp.co.takawagu.houseworkserver.model

import java.math.BigDecimal

data class TotalPrice(
        val userName: String,
        val totalPrice: BigDecimal
)