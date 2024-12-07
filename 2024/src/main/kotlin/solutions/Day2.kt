package adventofcode.solutions

import core.AoCDay
import kotlin.math.abs

object Day2 : AoCDay<List<String>>(2) {
    override fun transformInput(input: String): List<String> {
        return input.lineSequence()
            .toList()
    }

    override fun partOne(): Number {
        return input
            .map { line ->
                val levels = line.split(" ")
                    .map { it.toInt() }

                if (firstUnsafeReportIndex(levels) == -1) {
                    return@map 1
                }

                return@map 0
            }
            .sum()
    }

    override fun partTwo(): Int {
        return input
            .map { line ->
                val levels = line.split(" ")
                    .map { it.toInt() }

                val firstUnsafeReportIndex = firstUnsafeReportIndex(levels)
                if (firstUnsafeReportIndex(levels
                        .filterIndexed { index, _ ->  index != firstUnsafeReportIndex - 1}) == -1 ||
                    firstUnsafeReportIndex(levels
                        .filterIndexed { index, _ ->  index != firstUnsafeReportIndex}) == -1 ||
                    firstUnsafeReportIndex(levels
                        .filterIndexed { index, _ ->  index != firstUnsafeReportIndex + 1}) == -1) {
                    return@map 1
                }

                return@map 0
            }
            .sum()
    }

    private fun firstUnsafeReportIndex(levels: List<Int>): Int {
        for (i in 1..<levels.size - 1) {
            val diff1 = levels[i] - levels[i - 1]
            val diff2 = levels[i] - levels[i + 1]
            if(diff1 * diff2 >= 0 || abs(diff1) > 3 || abs(diff2) > 3) {
                return i
            }
        }

        return -1
    }
}