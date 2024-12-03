package adventofcode.solutions

import core.AoCDay
import kotlin.math.abs

class Day1 : AoCDay(1) {
    override fun partOne(input: String): Int {
        val leftList = mutableListOf<Int>()
        val rightList = mutableListOf<Int>()

        input
            .lineSequence()
            .map { line -> line.split("   ", limit = 2).map(String::toInt) }
            .forEach { (x, y) -> leftList.add(x); rightList.add(y) }

        leftList.sort()
        rightList.sort()

        var diff = 0
        while (leftList.isNotEmpty() || rightList.isNotEmpty()) {
            diff += abs(leftList.removeFirst() - rightList.removeFirst())
        }

        return diff
    }

    override fun partTwo(input: String): Int {
        val leftList = mutableListOf<Int>()
        val rightList = mutableListOf<Int>()

        input
            .lineSequence()
            .map { line -> line.split("   ", limit = 2).map(String::toInt) }
            .forEach { (x, y) -> leftList.add(x); rightList.add(y) }

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