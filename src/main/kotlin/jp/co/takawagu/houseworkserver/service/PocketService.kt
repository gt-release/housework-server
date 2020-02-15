package jp.co.takawagu.houseworkserver.service

import jp.co.takawagu.houseworkserver.model.*
import jp.co.takawagu.houseworkserver.repository.PocketRepository
import jp.co.takawagu.houseworkserver.repository.UsedRepository
import jp.co.takawagu.houseworkserver.repository.WorkRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth

@Service
class PocketService(private val workRepository: WorkRepository,
                    private val pocketRepository: PocketRepository,
                    private val usedRepository: UsedRepository) {
    fun aggregateData(req: PocketRequest) : Mono<List<Pocket>> {
       return Mono.zip(
               workRepository.findAllByUserNameOrderByWorkDate(req.userName).collectList(),
               usedRepository.findAllByUserNameOrderByUsedDate(req.userName).collectList()
       ) {a, b -> aggregate(YearMonth.of(2020, 2), a, b)}
    }

    fun sum() : Flux<TotalPrice> {

        return Mono.zip(pocketRepository.sumPriceGroupByUserName().collectList(),
                pocketRepository.sumUsedGroupByUserName().collectList())
        {a, b -> mergeSum(a, b)}.flatMapMany{Flux.fromIterable(it)}
    }

    private fun aggregate(selectedMonth: YearMonth, works: List<Work>, useds: List<UsedPrice>): List<Pocket> {
        val dates = mutableListOf<LocalDate>()
        var i = 1
        while(selectedMonth.isValidDay(i)) {
            dates.add(selectedMonth.atDay(i))
            i++
        }
        val result = mutableListOf<Pocket>()
        for(date in dates) {
            val total = works.filter{it.workDate <= date}.map{it.price}.fold(BigDecimal.ZERO, BigDecimal::add) -
                    useds.filter{it.usedDate <= date}.map{it.price}.fold(BigDecimal.ZERO, BigDecimal::add)
            result.add(Pocket(date, total))
        }
        return result.toList()
    }

    private fun mergeSum(plusPrice: List<TotalPrice>, minusPrice: List<TotalPrice>): List<TotalPrice> {
        val users = plusPrice.map { it.userName }.plus(minusPrice.map { it.userName }).toSet()
        println(users)
        return users.mapNotNull {
            name ->
            val plusElement = plusPrice.firstOrNull { it.userName == name }
            val minusElement = minusPrice.firstOrNull  { it.userName == name }
            if(plusElement != null && minusElement != null) {
                return@mapNotNull TotalPrice(name, plusElement.totalPrice - minusElement.totalPrice)
            } else if(plusElement != null && minusElement == null) {
                return@mapNotNull plusElement
            } else if(plusElement == null && minusElement != null) {
                return@mapNotNull minusElement
            } else {
                return@mapNotNull null
            }
        }
    }
}