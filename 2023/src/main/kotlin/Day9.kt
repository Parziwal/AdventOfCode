import java.io.File

fun main() {
    println("Day 9")
    println("-----------------------------------")
    println("Part One: ${partOneOfDay9()}")
    println("Part Two: ${partTwoOfDay9()}")
    println("-----------------------------------")
}

fun partOneOfDay9() = File("src/main/resources/day9.txt")
        .readLines()
        .sumOf { line ->
            val history = line.split(' ')
                .map { it.toInt() }
                .toMutableList()

            val temp = mutableListOf<Int>()
            var i = 0
            var extrapolatedValue = history.last()
            while (history.any { it != 0 }) {
                val next = history[i + 1] - history[i]
                temp.add(next)

                i++
                if (i == history.size - 1) {
                    i = 0
                    history.clear()
                    history.addAll(temp)
                    temp.clear()
                    extrapolatedValue += history.last()
                }
            }

            extrapolatedValue
        }

fun partTwoOfDay9() = File("src/main/resources/day9.txt")
    .readLines()
    .sumOf { line ->
        val history = line.split(' ')
            .map { it.toInt() }
            .toMutableList()

        val temp = mutableListOf<Int>()
        var extrapolatedValues = mutableListOf(history.first())
        var i = 0
        while (history.any { it != 0 }) {
            val next = history[i + 1] - history[i]
            temp.add(next)

            i++
            if (i == history.size - 1) {
                i = 0
                history.clear()
                history.addAll(temp)
                temp.clear()

                extrapolatedValues.add(history.first())
            }
        }

        extrapolatedValues.reversed()
            .reduce { acc, element ->  element - acc }
    }