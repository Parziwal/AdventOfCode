import java.io.File

fun main() {
    println("Day 6")
    println("-----------------------------------")
    println("Part One: ${partOneOfDay6()}")
    println("Part Two: ${partTwoOfDay6()}")
    println("-----------------------------------")
}

fun partOneOfDay6(): Int {
    val races = File("src/main/resources/day6.txt")
        .useLines { lines ->
            val listOfLines = lines.toList()
            val times = listOfLines[0].split(':')[1].split(' ')
                .filter { it.isNotEmpty() }
                .map { it.toInt() }
            val distances = listOfLines[1].split(':')[1].split(' ')
                .filter { it.isNotEmpty() }
                .map { it.toInt() }

            times.mapIndexed { index, time -> time to distances[index] }
                .toMap()
        }

    return races.map { race ->
        var possibilities = 0
        for (holdTime in 1..<race.key) {
            val distance = (race.key - holdTime) * holdTime
            if (distance > race.value) {
                possibilities++;
            }
        }

        possibilities
    }.reduce { acc, possibilities ->
        acc * possibilities
    }
}

fun partTwoOfDay6(): Int {
    val race = File("src/main/resources/day6.txt")
        .useLines { lines ->
            val listOfLines = lines.toList()
            val time = listOfLines[0].split(':')[1].split(' ')
                .filter { it.isNotEmpty() }
                .joinToString(separator = "") { it }
                .toLong()
            val distance = listOfLines[1].split(':')[1].split(' ')
                .filter { it.isNotEmpty() }
                .joinToString(separator = "") { it }
                .toLong()

            Pair(time, distance)
        }

    var possibilities = 0
    for (holdTime in 1..<race.first) {
        val distance = (race.first - holdTime) * holdTime
        if (distance > race.second) {
            possibilities++;
        }
    }

    return possibilities
}