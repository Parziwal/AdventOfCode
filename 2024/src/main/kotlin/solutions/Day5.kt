package adventofcode.solutions

import core.AoCDay

object Day5 : AoCDay<Pair<MutableMap<Int, MutableList<Int>>, MutableList<List<Int>>>>(5) {
    override fun transformInput(input: String): Pair<MutableMap<Int, MutableList<Int>>, MutableList<List<Int>>> {
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

    override fun partOne(): Number {
        val (numbersComeBefore, pageNumberSequences) = input

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

    override fun partTwo(): Number {
        val (numbersComeBefore, pageNumberSequences) = input

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
}