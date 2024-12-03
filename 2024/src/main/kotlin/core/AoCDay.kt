package core

import java.io.File

abstract class AoCDay(val dayNumber: Int) {
    private val input: String by lazy {
        File("src/main/resources/inputs/day$dayNumber.txt")
            .readText()
    }

    fun printPartOne() {
        val partOneResult = partOne(input)
        when {
            partOneResult != null -> {
                println("Day $dayNumber |> Part One: $partOneResult")
            }
            else -> {
                println("Day $dayNumber |> Part One: NOT IMPLEMENTED")
            }
        }
    }

    fun printPartTwo() {
        val partTwoResult = partTwo(input)
        when {
            partTwoResult != null -> {
                println("Day $dayNumber |> Part Two: $partTwoResult")
            }
            else -> {
                println("Day $dayNumber |> Part Two: NOT IMPLEMENTED")
            }
        }
    }

    abstract fun partOne(input: String): Number?
    abstract fun partTwo(input: String): Number?
}