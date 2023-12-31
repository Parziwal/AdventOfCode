import java.io.File
import kotlin.math.abs

fun main() {
    println("Day 10")
    println("-----------------------------------")
    println("Part One: ${partOneOfDay10()}")
    println("Part Two: ${partTwoOfDay10()}")
    println("-----------------------------------")
}

val NorthPipes = mapOf(
    '|' to PipeMapPosition(0, -1),
    '7' to PipeMapPosition(-1, 0),
    'F' to PipeMapPosition(1, 0),
)
val EastPipes = mapOf(
    '-' to PipeMapPosition(1, 0),
    'J' to PipeMapPosition(0, -1),
    '7' to PipeMapPosition(0, 1),
)
val SouthPipes = mapOf(
    '|' to PipeMapPosition(0, 1),
    'L' to PipeMapPosition(1, 0),
    'J' to PipeMapPosition(-1, 0),
)
val WestPipes = mapOf(
    '-' to PipeMapPosition(-1, 0),
    'L' to PipeMapPosition(0, -1),
    'F' to PipeMapPosition(0, 1),
)

data class PipeMapPosition(val x: Int, val y: Int)

fun PipeMapPosition.nextPosition(previous: PipeMapPosition, pipeMap: List<CharArray>): PipeMapPosition {
    val direction = when {
        previous.y > this.y && NorthPipes.contains(pipeMap[this.y][this.x]) -> NorthPipes[pipeMap[this.y][this.x]]!!
        previous.x < this.x && EastPipes.contains(pipeMap[this.y][this.x]) -> EastPipes[pipeMap[this.y][this.x]]!!
        previous.y < this.y && SouthPipes.contains(pipeMap[this.y][this.x]) -> SouthPipes[pipeMap[this.y][this.x]]!!
        previous.x > this.x && WestPipes.contains(pipeMap[this.y][this.x]) -> WestPipes[pipeMap[this.y][this.x]]!!
        else -> throw Exception()
    }

    return PipeMapPosition(this.x + direction.x, this.y + direction.y)
}

fun PipeMapPosition.startPositions(pipeMap: List<CharArray>): List<PipeMapPosition> {
    val startPositions = mutableListOf<PipeMapPosition>()
    if (NorthPipes.contains(pipeMap[this.y - 1][this.x])) {
        startPositions.add(PipeMapPosition(x, y - 1))
    }
    if (EastPipes.contains(pipeMap[this.y][this.x + 1])) {
        startPositions.add(PipeMapPosition(x + 1, y))
    }
    if (SouthPipes.contains(pipeMap[this.y + 1][this.x])) {
        startPositions.add(PipeMapPosition(x, y + 1))
    }
    if (WestPipes.contains(pipeMap[this.y][this.x - 1])) {
        startPositions.add(PipeMapPosition(x - 1, y))
    }

    return startPositions
}

fun partOneOfDay10(): Int {
    val pipeMap = File("src/main/resources/day10.txt")
        .readLines()
        .map { it.toCharArray() }

    lateinit var start: PipeMapPosition
    for ((y, row) in pipeMap.withIndex()) {
        val x = row.indexOf('S')
        if (x != -1) {
            start = PipeMapPosition(x, y)
        }
    }

    var step = 1
    var (path1, path2) = start.startPositions(pipeMap)
    var previous1 = start
    var previous2 = start

    while (path1.x != path2.x || path1.y != path2.y) {
        step++
        val tempPrevious1 = path1
        val tempPrevious2 = path2

        path1 = path1.nextPosition(previous1, pipeMap)
        path2 = path2.nextPosition(previous2, pipeMap)

        previous1 = tempPrevious1
        previous2 = tempPrevious2
        pipeMap[tempPrevious1.y][tempPrevious1.x] = 'X'
        pipeMap[tempPrevious2.y][tempPrevious2.x] = 'X'
    }

    return step
}

fun partTwoOfDay10(): Int {
    val pipeMap = File("src/main/resources/day10.txt")
        .readLines()
        .map { it.toCharArray() }

    lateinit var start: PipeMapPosition
    for ((y, row) in pipeMap.withIndex()) {
        val x = row.indexOf('S')
        if (x != -1) {
            start = PipeMapPosition(x, y)
        }
    }

    var (path) = start.startPositions(pipeMap)
    var previous = start

    val loopPoints = mutableListOf(start, path)
    while (path.x != start.x || path.y != start.y) {
        val tempPrevious = path

        path = path.nextPosition(previous, pipeMap)

        previous = tempPrevious
        pipeMap[tempPrevious.y][tempPrevious.x] = 'X'

        loopPoints.add(path)
    }

    var area = 0
    for (i in 0..<loopPoints.size - 1) {
        area += (loopPoints[i].y + loopPoints[i + 1].y) * (loopPoints[i].x - loopPoints[i + 1].x)
    }

   return (abs(area) - (loopPoints.size - 1)) / 2 + 1
}