import java.io.File
import kotlin.math.max
import kotlin.math.min

fun main() {
    println("Day 3")
    println("-----------------------------------")
    println("Part One: ${partOneOfDay3()}")
    println("Part Two: ${partTwoOfDay3()}")
    println("-----------------------------------")
}

data class PartPosition(val x: Int, val y: Int)

fun partOneOfDay3(): Int {
    val engineSchematic = File("src/main/resources/day3.txt").readLines()

    return engineSchematic.mapIndexed { y, line ->
        var startOfNumber: Int? = null
        var endOfNumber: Int? = null
        var sum = 0
        line.forEachIndexed { x, letter ->
            if (startOfNumber == null && letter.isDigit()) {
                startOfNumber = x
            }
            if (startOfNumber != null && line.length - 1 == x) {
                endOfNumber = x
            }
            if (startOfNumber != null && !letter.isDigit()) {
                endOfNumber = x - 1
            }

            if (startOfNumber != null && endOfNumber != null) {
                val hasSymbol = (max(0, y - 1)..min(engineSchematic.size - 1, y + 1))
                    .any { i ->
                        engineSchematic[i]
                            .substring(max(0, startOfNumber!! - 1)..min(line.length - 1, endOfNumber!! + 1))
                            .any { it != '.' && !it.isDigit() }
                    }

                if (hasSymbol) {
                    val partNumber = line.substring(startOfNumber!!..endOfNumber!!).toInt()
                    sum += partNumber
                }
                startOfNumber = null
                endOfNumber = null
            }
        }

        sum
    }.sum()
}

fun partTwoOfDay3(): Int {
    val engineSchematic = File("src/main/resources/day3.txt").readLines()
    return engineSchematic.mapIndexed { y, line ->
        var gearPosition = 0
        var sumOfGearRatio = 0
        while (true) {
            gearPosition = line.indexOf('*', startIndex = gearPosition + 1)
            if (gearPosition == -1) {
                break
            }

            val adjacentParts = mutableListOf<PartPosition>()
            for (i in max(0,y - 1)..min(engineSchematic.size - 1, y + 1)) {
                var isPreviousDigit = false
                for (j in max(0,gearPosition - 1)..min(line.length - 1, gearPosition + 1)) {
                    if (engineSchematic[i][j].isDigit() && !isPreviousDigit) {
                        val partPosition = PartPosition(j, i)
                        adjacentParts.add(partPosition)
                        isPreviousDigit = true
                    }

                    if (!engineSchematic[i][j].isDigit()) {
                        isPreviousDigit = false
                    }
                }
            }

            if (adjacentParts.size == 2) {
                val gearRatio = adjacentParts.map { pos ->
                    var partNumber = ""
                    for (letter in engineSchematic[pos.y].substring(pos.x..<line.length)) {
                        if (letter.isDigit()) {
                            partNumber += letter
                        } else {
                            break
                        }
                    }

                    for (letter in engineSchematic[pos.y].substring(0..<pos.x).reversed()) {
                        if (letter.isDigit()) {
                            partNumber = letter.plus(partNumber)
                        } else {
                            break
                        }
                    }
                    partNumber.toInt()
                }.reduce { accumulator, element ->
                    accumulator * element
                }
                sumOfGearRatio += gearRatio
            }
        }

        sumOfGearRatio
    }.sum()
}