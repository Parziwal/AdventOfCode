import java.io.File

fun main() {
    println("Day 11")
    println("-----------------------------------")
    println("Part One: ${partOneOfDay11()}")
    println("Part Two: ${partTwoOfDay11()}")
    println("-----------------------------------")
}

data class Galaxy(val x: Int, val y: Int)

fun sumOfLengthBetweenGalaxies(expansionSize: Long): Long {
    val universe = File("src/main/resources/day11.txt")
        .readLines()

    val expandedUniverse = universe.map { line ->
        if (line.none { it == '#' }) {
            "X".repeat(line.length)
        } else {
            line.mapIndexed { i, cell -> if (universe.none { it[i] == '#' }) 'X' else cell }
                .joinToString("")
        }
    }

    val galaxies = expandedUniverse.flatMapIndexed { y, line ->
        line.mapIndexedNotNull { x, c ->
            if (c == '#') Galaxy(x, y) else null
        }
    }

    return galaxies.flatMapIndexed { i, g1 ->
        galaxies.drop(i)
            .map { g2 ->
                val xIndices = if (g1.x > g2.x) g1.x - 1 downTo g2.x else g1.x..<g2.x
                val yIndices = if (g1.y > g2.y) g1.y - 1 downTo g2.y else g1.y..<g2.y
                val xSteps = xIndices.sumOf { x -> if (expandedUniverse[g1.y][x] == 'X') expansionSize else 1 }
                val ySteps = yIndices.sumOf { y -> if (expandedUniverse[y][g1.x] == 'X') expansionSize else 1 }

                xSteps + ySteps
            }
    }.sum()
}

fun partOneOfDay11() = sumOfLengthBetweenGalaxies(2)
fun partTwoOfDay11() = sumOfLengthBetweenGalaxies(1000000)