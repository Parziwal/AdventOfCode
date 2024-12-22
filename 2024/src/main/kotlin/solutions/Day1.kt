package adventofcode.solutions

import core.AoCDay
import kotlin.math.abs

object Day1 : AoCDay<Pair<List<Int>, List<Int>>>(1) {
    override fun transformInput(input: String): Pair<List<Int>, List<Int>> {
        val leftList = mutableListOf<Int>()
        val rightList = mutableListOf<Int>()

        input
            .lineSequence()
            .map { line -> line.split("   ", limit = 2).map(String::toInt) }
            .forEach { (x, y) -> leftList.add(x); rightList.add(y) }

        return Pair(leftList, rightList)
    }

    override fun partOne(): Number {
        val (leftList, rightList) = input.let {
            Pair(it.first.toMutableList(), it.second.toMutableList())
        }

        leftList.sort()
        rightList.sort()

        var diff = 0
        while (leftList.isNotEmpty() || rightList.isNotEmpty()) {
            diff += abs(leftList.removeFirst() - rightList.removeFirst())
        }

        return diff
    }

    override fun partTwo(): Number {
        val (leftList, rightList) = input.let {
            Pair(it.first.toMutableList(), it.second.toMutableList())
        }

        val locationIdOccurrencesInLeftList = rightList
            .groupingBy { id -> id }
            .eachCount()

        var similarityScore = 0
        for (id in leftList) {
            similarityScore += (locationIdOccurrencesInLeftList[id] ?: 0) * id
        }

        return similarityScore
    }
}