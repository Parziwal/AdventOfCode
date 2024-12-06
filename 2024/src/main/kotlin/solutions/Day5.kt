package adventofcode.solutions

import core.AoCDay

class Day5 : AoCDay(5) {
    override fun partOne(input: String): Number {
        val (numbersComeBefore, pageNumberSequences) = readInput(input)

        return pageNumberSequences.map { pageNumberSequence ->
            for (i in 0..<pageNumberSequence.size - 1) {
                numbersComeBefore[pageNumberSequence[i]]?.let {
                    for (j in (i + 1)..<pageNumberSequence.size) {
                        if (it.contains(pageNumberSequence[j])) {
                            return@map 0
                        }
                    }
                }
            }

            return@map pageNumberSequence[pageNumberSequence.size / 2]
        }.sum()
    }

    override fun partTwo(input: String): Number {
        val (numbersComeBefore, pageNumberSequences) = readInput(input)

        return pageNumberSequences.map { pageNumberSequence ->
            var isOrderRight = true
            val updatedPageNumberSequence = pageNumberSequence.toMutableList()
            var i = 0
            while (i < updatedPageNumberSequence.size - 1) {
                numbersComeBefore[updatedPageNumberSequence[i]]?.let {
                    var currentIndexOfUpdatedPage = i
                    for (j in (i + 1)..<updatedPageNumberSequence.size) {
                        if (it.contains(updatedPageNumberSequence[j])) {
                            isOrderRight = false
                            val temp = updatedPageNumberSequence[currentIndexOfUpdatedPage]
                            updatedPageNumberSequence[currentIndexOfUpdatedPage] = updatedPageNumberSequence[j]
                            updatedPageNumberSequence[j] = temp
                            currentIndexOfUpdatedPage = j
                        }
                    }

                    if (i != currentIndexOfUpdatedPage) {
                        i--
                    }
                }
                i++
            }

            if (!isOrderRight) {
                return@map updatedPageNumberSequence[pageNumberSequence.size / 2]
            } else {
                return@map 0
            }
        }.sum()
    }

    private fun readInput(input: String): Pair<MutableMap<Int, MutableList<Int>>, MutableList<List<Int>>> {
        val numbersComeBefore = mutableMapOf<Int, MutableList<Int>>()
        val pageNumberSequences = mutableListOf<List<Int>>()
        var readFirstPart = true

        input
            .lineSequence()
            .forEach { line ->
                if (line.isEmpty()) {
                    readFirstPart = false
                    return@forEach
                }

                if (readFirstPart) {
                    val (beforeNumber, afterNumber) = line.split("|")
                        .map { it.toInt() }

                    numbersComeBefore[afterNumber]?.add(beforeNumber)

                    if (!numbersComeBefore.containsKey(afterNumber)) {
                        numbersComeBefore[afterNumber] = mutableListOf(beforeNumber)
                    }
                } else {
                    val pageNumbers = line.split(",")
                        .map { it.toInt() }
                    pageNumberSequences.add(pageNumbers)
                }
            }

        return Pair(numbersComeBefore, pageNumberSequences)
    }
}