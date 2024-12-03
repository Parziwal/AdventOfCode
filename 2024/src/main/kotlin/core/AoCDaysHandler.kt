package adventofcode.core

import adventofcode.solutions.*

object AoCDaysHandler {
    private val days = listOf(
        Day1(),
        Day2(),
        Day3(),
    )

    fun printDay(dayNumber: Int? = null) {
        val choseDay = if (dayNumber != null) {
            days.find { it.dayNumber == dayNumber } ?: throw Exception("Day not exists!")
        } else {
            days.last()
        }

        choseDay.printPartOne()
        choseDay.printPartTwo()
    }

    fun printAllDays() {
        days.forEach {
            it.printPartOne()
            it.printPartTwo()
            println("--------------------")
        }
    }
}