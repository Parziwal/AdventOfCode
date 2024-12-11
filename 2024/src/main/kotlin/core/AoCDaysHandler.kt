package adventofcode.core

import adventofcode.solutions.*
import core.AoCDay

object AoCDaysHandler {
    private val days = listOf(
        Day1,
        Day2,
        Day3,
        Day4,
        Day5,
        Day6,
        Day7,
        Day8,
        Day9,
        Day10,
        Day11,
    )

    fun printDay(dayNumber: Int) {
        printDay(days[dayNumber - 1])
    }

    fun printLatestDay() {
        printDay(days.last())
    }

    fun printAllDays() {
        days.forEach {
            printDay(it)
        }
    }

    private fun printDay(day: AoCDay<Any>) {
        var exception: Exception? = null

        val partOneSolution = try {
            day.partOne().toString()
        } catch (e: NotImplementedError) {
            "NOT IMPLEMENTED"
        } catch (e: Exception) {
            exception = e
            e.toString()
        }

        println("Day ${day.dayNumber} |> Part One: $partOneSolution")
        exception?.printStackTrace()

        val partTwoSolution = try {
            day.partTwo().toString()
        } catch (e: NotImplementedError) {
            "NOT IMPLEMENTED"
        } catch (e: Exception) {
            exception = e
            e.toString()
        }

        println("Day ${day.dayNumber} |> Part Two: $partTwoSolution")
        exception?.printStackTrace()
    }
}