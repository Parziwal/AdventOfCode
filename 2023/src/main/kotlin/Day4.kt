import java.io.File

fun main() {
    println("Day 4")
    println("-----------------------------------")
    println("Part One: ${partOneOfDay4()}")
    println("Part Two: ${partTwoOfDay4()}")
    println("-----------------------------------")
}

fun partOneOfDay4() = File("src/main/resources/day4.txt")
        .readLines()
        .sumOf { line ->
            val winningNumbers = line.split(':')[1]
                .split('|')[0]
                .split(' ')
                .filter { it.isNotEmpty() }

            line.split(':')[1]
                .split('|')[1]
                .split(' ')
                .filter { it.isNotEmpty() }
                .fold(0) { acc, number ->
                    if (winningNumbers.contains(number)) {
                        return@fold if (acc == 0) 1 else acc * 2
                    }
                    acc
                }
                .toInt()
        }

fun partTwoOfDay4(): Int {
    val scratchcards = File("src/main/resources/day4.txt")
        .readLines()
    val scratchcardCount = mutableMapOf<Int, Int>().apply {
        repeat(scratchcards.size) { index ->
            put(index, 1)
        }
    }
    scratchcards.forEachIndexed { lineNumber, line ->
        val winningNumbers = line.split(':')[1]
            .split('|')[0]
            .split(' ')
            .filter { it.isNotEmpty() }

        val matches = line.split(':')[1]
            .split('|')[1]
            .split(' ')
            .count { it.isNotEmpty() && winningNumbers.contains(it) }

        repeat(matches) { index ->
            if (lineNumber + index + 1 < scratchcards.size) {
                scratchcardCount[lineNumber + index + 1] = scratchcardCount[lineNumber + index + 1]!! + scratchcardCount[lineNumber]!!
            }
        }
    }

    return scratchcardCount.values.sum()
}