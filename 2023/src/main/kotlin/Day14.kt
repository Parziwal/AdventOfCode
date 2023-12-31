import java.io.File

fun main() {
    println("Day 14")
    println("-----------------------------------")
    println("Part One: ${partOneOfDay14()}")
    println("Part Two: ${partTwoOfDay14()}")
    println("-----------------------------------")
}

enum class PlatformTiltDirection {
    North,
    West,
    South,
    East,
}

fun MutableList<String>.tiltPlatform(direction: PlatformTiltDirection) {
    when (direction) {
        PlatformTiltDirection.East -> {
            for (i in this.indices) {
                var obstacle = this.first().length - 1
                for (j in this.first().indices.reversed()) {
                    if (this[i][j] == '#') {
                        obstacle = j - 1
                    } else if (this[i][j] == 'O') {
                        this[i] = this[i].replaceRange(j..j, ".")
                        this[i] = this[i].replaceRange(obstacle..obstacle, "O")
                        obstacle--
                    }
                }
            }
        }
        PlatformTiltDirection.North -> {
            for (i in this.first().indices) {
                var obstacle = 0
                for (j in this.indices) {
                    if (this[j][i] == '#') {
                        obstacle = j + 1
                    } else if (this[j][i] == 'O') {
                        this[j] = this[j].replaceRange(i..i, ".")
                        this[obstacle] = this[obstacle].replaceRange(i..i, "O")
                        obstacle++
                    }
                }
            }
        }
        PlatformTiltDirection.West -> {
            for (i in this.indices) {
                var obstacle = 0
                for (j in this.first().indices) {
                    if (this[i][j] == '#') {
                        obstacle = j + 1
                    } else if (this[i][j] == 'O') {
                        this[i] = this[i].replaceRange(j..j, ".")
                        this[i] = this[i].replaceRange(obstacle..obstacle, "O")
                        obstacle++
                    }
                }
            }
        }
        PlatformTiltDirection.South -> {
            for (i in this.first().indices) {
                var obstacle = this.size - 1
                for (j in this.indices.reversed()) {
                    if (this[j][i] == '#') {
                        obstacle = j - 1
                    } else if (this[j][i] == 'O') {
                        this[j] = this[j].replaceRange(i..i, ".")
                        this[obstacle] = this[obstacle].replaceRange(i..i, "O")
                        obstacle--
                    }
                }
            }
        }
    }
}

fun List<String>.calculateLoad(): Int {
    return this.mapIndexed { i, row ->
        val count = row.count { it == 'O' }
        count * (this.size - i)
    }.sum()
}

fun partOneOfDay14(): Int {
    val platform = File("src/main/resources/day14.txt")
        .readLines()
        .toMutableList()

    for (i in platform.first().indices) {
        var obstacle = 0
        for (j in platform.indices) {
            if (platform[j][i] == '#') {
                obstacle = j + 1
            } else if (platform[j][i] == 'O') {
                platform[j] = platform[j].replaceRange(i..i, ".")
                platform[obstacle] = platform[obstacle].replaceRange(i..i, "O")
                obstacle++
            }
        }
    }

    return platform.calculateLoad()
}

fun partTwoOfDay14(): Int {
    val platform = File("src/main/resources/day14.txt")
        .readLines()
        .toMutableList()

    val cache = mutableMapOf<String, Int>()
    val totalCycles = 1_000_000_000
    run repeatBlock@{
        repeat(totalCycles) { i ->
            val key = platform.joinToString("")
            if (cache.contains(key)) {
                val length = i - cache.getValue(key)
                val remainingCycles = (totalCycles - i) % length
                repeat(remainingCycles) {
                    PlatformTiltDirection.entries.forEach { direction ->
                        platform.tiltPlatform(direction)
                    }
                }
                return@repeatBlock
            }

            PlatformTiltDirection.entries.forEach { direction ->
                platform.tiltPlatform(direction)
            }
            cache[key] = i
        }
    }

    return platform.calculateLoad()
}