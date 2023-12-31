import java.io.File

fun main() {
    println("Day 2")
    println("-----------------------------------")
    println("Part One: ${partOneOfDay2()}")
    println("Part Two: ${partTwoOfDay2()}")
    println("-----------------------------------")
}

fun partOneOfDay2(): Int {
    val loadedCubes = mapOf("red" to 12, "green" to 13, "blue" to 14)

    return File("src/main/resources/day2.txt")
        .readLines()
        .sumOf { line ->
            val gameId = line.split(": ")[0].split(' ')[1].toInt()
            val subsetsOfCubes = line.split(": ")[1]
            val isImpossible = subsetsOfCubes.split(", ", "; ")
                .any { cube ->
                    val count = cube.split(' ')[0].toInt()
                    val color = cube.split(' ')[1]

                    loadedCubes[color]!! < count
                }

            if (isImpossible) 0 else gameId
        }
}

fun partTwoOfDay2(): Int =
    File("src/main/resources/day2.txt")
        .readLines()
        .sumOf { line ->
            val subsetsOfCubes = line.split(": ")[1]
            val minNumberOfCubes = mutableMapOf("red" to 0, "green" to 0, "blue" to 0)
            subsetsOfCubes.split(", ", "; ")
                .forEach { cube ->
                    val count = cube.split(' ')[0].toInt()
                    val color = cube.split(' ')[1]

                    if (minNumberOfCubes[color]!! < count) {
                        minNumberOfCubes[color] = count
                    }
                }

            minNumberOfCubes.values.reduce { accumulator, element ->
                accumulator * element
            }
        }