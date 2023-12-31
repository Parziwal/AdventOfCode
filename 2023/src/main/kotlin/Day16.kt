import java.io.File
import kotlin.math.max

fun main() {
    println("Day 16")
    println("-----------------------------------")
    println("Part One: ${partOneOfDay16()}")
    println("Part Two: ${partTwoOfDay16()}")
    println("-----------------------------------")
}

enum class BeamDirection {
    Up,
    Down,
    Left,
    Right,
}

data class BeamPosition(val x: Int, val y:Int, val direction: BeamDirection)

fun calculateBeam(x: Int, y: Int, direction: BeamDirection, positions: MutableSet<BeamPosition>,
                  layout: List<String>, energizedMap: MutableList<String>) {
    energizedMap[y] = energizedMap[y].replaceRange(x..x, "#")
    val nextDirections = layout[y][x].nextDirections(direction)

    for (dir in nextDirections) {
        val pos = BeamPosition(x, y, dir)
        if (positions.contains(pos)) {
            continue
        }

        positions.add(pos)
        when {
            dir == BeamDirection.Up && y > 0 -> {
                calculateBeam(x, y - 1, dir, positions, layout, energizedMap)
            }
            dir == BeamDirection.Down && y < layout.size - 1 -> {
                calculateBeam(x, y + 1, dir, positions, layout, energizedMap)
            }
            dir == BeamDirection.Left && x > 0 -> {
                calculateBeam(x - 1, y, dir, positions, layout, energizedMap)
            }
            dir == BeamDirection.Right && x < layout.first().length - 1 -> {
                calculateBeam(x + 1, y, dir, positions, layout, energizedMap)
            }
        }
    }
}

fun Char.nextDirections(direction: BeamDirection): List<BeamDirection> {
    return when {
        direction == BeamDirection.Up && this == '.' -> listOf(BeamDirection.Up)
        direction == BeamDirection.Up && this == '/' -> listOf(BeamDirection.Right)
        direction == BeamDirection.Up && this == '\\' -> listOf(BeamDirection.Left)
        direction == BeamDirection.Up && this == '-' -> listOf(BeamDirection.Left, BeamDirection.Right)
        direction == BeamDirection.Up && this == '|' -> listOf(BeamDirection.Up)
        direction == BeamDirection.Down && this == '.' -> listOf(BeamDirection.Down)
        direction == BeamDirection.Down && this == '/' -> listOf(BeamDirection.Left)
        direction == BeamDirection.Down && this == '\\' -> listOf(BeamDirection.Right)
        direction == BeamDirection.Down && this == '-' -> listOf(BeamDirection.Left, BeamDirection.Right)
        direction == BeamDirection.Down && this == '|' -> listOf(BeamDirection.Down)
        direction == BeamDirection.Left && this == '.' -> listOf(BeamDirection.Left)
        direction == BeamDirection.Left && this == '/' -> listOf(BeamDirection.Down)
        direction == BeamDirection.Left && this == '\\' -> listOf(BeamDirection.Up)
        direction == BeamDirection.Left && this == '-' -> listOf(BeamDirection.Left)
        direction == BeamDirection.Left && this == '|' -> listOf(BeamDirection.Up, BeamDirection.Down)
        direction == BeamDirection.Right && this == '.' -> listOf(BeamDirection.Right)
        direction == BeamDirection.Right && this == '/' -> listOf(BeamDirection.Up)
        direction == BeamDirection.Right && this == '\\' -> listOf(BeamDirection.Down)
        direction == BeamDirection.Right && this == '-' -> listOf(BeamDirection.Right)
        direction == BeamDirection.Right && this == '|' -> listOf(BeamDirection.Up, BeamDirection.Down)
        else -> listOf()
    }
}

fun List<String>.getEnergizedFieldsCount(): Int {
    return this.sumOf { row ->
        row.count { it == '#' }
    }
}

fun partOneOfDay16(): Int {
    val contraptionLayout = File("src/main/resources/day16.txt")
        .readLines()

    val energizedMap = contraptionLayout.toMutableList()
    calculateBeam(0, 0, BeamDirection.Right, mutableSetOf(), contraptionLayout, energizedMap)

    return energizedMap.getEnergizedFieldsCount()
}

fun partTwoOfDay16(): Int {
    val contraptionLayout = File("src/main/resources/day16.txt")
        .readLines()

    var maxEnergizedFieldsCount = 0
    for (i in contraptionLayout.indices) {
        var energizedMap = contraptionLayout.toMutableList()

        calculateBeam(0, i, BeamDirection.Right, mutableSetOf(), contraptionLayout, energizedMap)
        maxEnergizedFieldsCount = max(maxEnergizedFieldsCount, energizedMap.getEnergizedFieldsCount())
        energizedMap = contraptionLayout.toMutableList()

        calculateBeam(contraptionLayout.size - 1, i, BeamDirection.Left, mutableSetOf(), contraptionLayout, energizedMap)
        maxEnergizedFieldsCount = max(maxEnergizedFieldsCount, energizedMap.getEnergizedFieldsCount())
        energizedMap = contraptionLayout.toMutableList()

        calculateBeam(i, contraptionLayout.size - 1, BeamDirection.Up, mutableSetOf(), contraptionLayout, energizedMap)
        maxEnergizedFieldsCount = max(maxEnergizedFieldsCount, energizedMap.getEnergizedFieldsCount())
        energizedMap = contraptionLayout.toMutableList()

        calculateBeam(i, 0, BeamDirection.Down, mutableSetOf(), contraptionLayout, energizedMap)
        maxEnergizedFieldsCount = max(maxEnergizedFieldsCount, energizedMap.getEnergizedFieldsCount())
    }

    return maxEnergizedFieldsCount
}