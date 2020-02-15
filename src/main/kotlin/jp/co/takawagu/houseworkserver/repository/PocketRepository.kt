package jp.co.takawagu.houseworkserver.repository

import jp.co.takawagu.houseworkserver.model.TotalPrice
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository

@Repository
class PocketRepository (private val client: DatabaseClient) {

    fun sumPriceGroupByUserName() = client.
            execute("""
                select 
                    user_name, 
                    sum(price) as total_price 
                from 
                    work
                group by
                    user_name
            """.trimIndent()
                    ).
            `as`(TotalPrice::class.java).
            fetch().
            all()

    fun sumUsedGroupByUserName() = client.
            execute("""
                select 
                    user_name, 
                    sum(price) as total_price 
                from 
                    used_price
                group by
                    user_name
            """.trimIndent()
            ).
            `as`(TotalPrice::class.java).
            fetch().
            all()
}