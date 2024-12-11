package adventofcode.solutions

import core.AoCDay
import kotlin.math.pow

object Day11 : AoCDay<List<Day11.Stone>>(11) {
    override fun transformInput(input: String): List<Stone> {
        return input.split(" ")
            .map { Stone(it.toLong(), 1) }
            .toMutableList()
    }

    override fun partOne(): Number {
        return stoneCountAfterBlinking(25)

    }

    override fun partTwo(): Number {
        return stoneCountAfterBlinking(75)
    }

    private fun stoneCountAfterBlinking(blinkingTimes: Int): Long {
        var stones = input.toMutableList()
        repeat(blinkingTimes) {
            var i = 0
            while (i < stones.size) {
                when {
                    stones[i].engravedNumber == 0L -> {
                        stones[i] = stones[i].copy(engravedNumber = 1L)
                    }
                    stones[i].engravedNumber.toString().length % 2 == 0 -> {
                        val halfOfStoneDigitsCount = stones[i].engravedNumber.toString().length / 2
                        val firstHalfOfStone = stones[i].engravedNumber / 10.0.pow(halfOfStoneDigitsCount).toLong()
                        val secondHalfOfStone = stones[i].engravedNumber % 10.0.pow(halfOfStoneDigitsCount).toLong()

                        stones[i] = stones[i].copy(engravedNumber = firstHalfOfStone)
                        stones.add(i + 1, stones[i].copy(engravedNumber = secondHalfOfStone))
                        i++
                    }
                    else -> {
                        stones[i] = stones[i].copy(engravedNumber = stones[i].engravedNumber * 2024L)
                    }
                }

                i++
            }

            stones = stones.groupBy { it.engravedNumber }
                .map { stone ->
                    Stone(stone.key, stone.value.sumOf { it.count })
                }
                .toMutableList()
        }

        return stones.sumOf { it.count }
    }

    data class Stone(val engravedNumber: Long, val count: Long)
}