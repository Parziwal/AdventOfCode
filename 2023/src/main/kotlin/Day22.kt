import java.io.File
import kotlin.math.max
import kotlin.math.min

fun main() {
    println("Day 22")
    println("-----------------------------------")
    println("Part One: ${partOneOfDay22()}")
    println("Part Two: ${partTwoOfDay22()}")
    println("-----------------------------------")
}

data class SandBrick(val xRange: IntRange, val yRange: IntRange, var zRange: IntRange) {
    fun isCollideOn2D(brick: SandBrick): Boolean {
        return xRange.overlaps(brick.xRange) && yRange.overlaps(brick.yRange)
    }
}

private fun ClosedRange<Int>.overlaps(other: ClosedRange<Int>): Boolean {
    return max(this.start, other.start) <= min(this.endInclusive, other.endInclusive)
}

fun partOneOfDay22(): Int {
    var sandBricks = File("src/main/resources/day22.txt")
        .readLines()
        .map { line ->
            val startCoordinates = line.split('~')[0]
                .split(',')
                .map { it.toInt() }
            val endCoordinates = line.split('~')[1]
                .split(',')
                .map { it.toInt() }

            SandBrick(
                startCoordinates[0]..endCoordinates[0],
                startCoordinates[1]..endCoordinates[1],
                startCoordinates[2]..endCoordinates[2],
            )
        }
        .sortedBy { it.zRange.first }

    sandBricks.forEachIndexed { index, brick ->
        var maxZ = 1
        for (check in index - 1 downTo 0) {
            if (brick.isCollideOn2D(sandBricks[check]))
            {
                maxZ = max(maxZ, sandBricks[check].zRange.last + 1)
            }
        }
        brick.zRange = maxZ..maxZ + brick.zRange.last - brick.zRange.first
    }

    sandBricks = sandBricks.sortedBy { it.zRange.first }
    val supporterBricks = mutableMapOf<Int, MutableList<Int>>()
        .apply {
            repeat(sandBricks.size) { i ->
                put(i, mutableListOf())
            }
        }
    val bricksToSupport = mutableMapOf<Int, MutableList<Int>>()
        .apply {
            repeat(sandBricks.size) { i ->
                put(i, mutableListOf())
            }
        }

    for (i in sandBricks.indices) {
        for (j in 0..<i) {
            if (sandBricks[i].isCollideOn2D(sandBricks[j]) && sandBricks[i].zRange.first == sandBricks[j].zRange.last + 1) {
                supporterBricks[i]!!.add(j)
                bricksToSupport[j]!!.add(i)
            }
        }
    }

    var safeBrickCount = 0
    for (i in sandBricks.indices) {
        if (bricksToSupport[i]!!.all { supporterBricks[it]!!.size >= 2 }) {
            safeBrickCount++
        }
    }

    return safeBrickCount
}

fun partTwoOfDay22(): Int {
    var sandBricks = File("src/main/resources/day22.txt")
        .readLines()
        .map { line ->
            val startCoordinates = line.split('~')[0]
                .split(',')
                .map { it.toInt() }
            val endCoordinates = line.split('~')[1]
                .split(',')
                .map { it.toInt() }

            SandBrick(
                startCoordinates[0]..endCoordinates[0],
                startCoordinates[1]..endCoordinates[1],
                startCoordinates[2]..endCoordinates[2],
            )
        }
        .sortedBy { it.zRange.first }

    sandBricks.forEachIndexed { index, brick ->
        var maxZ = 1
        for (check in index - 1 downTo 0) {
            if (brick.isCollideOn2D(sandBricks[check]))
            {
                maxZ = max(maxZ, sandBricks[check].zRange.last + 1)
            }
        }
        brick.zRange = maxZ..maxZ + brick.zRange.last - brick.zRange.first
    }

    sandBricks = sandBricks.sortedBy { it.zRange.first }
    val supporterBricks = mutableMapOf<Int, MutableList<Int>>()
        .apply {
            repeat(sandBricks.size) { i ->
                put(i, mutableListOf())
            }
        }
    val bricksToSupport = mutableMapOf<Int, MutableList<Int>>()
        .apply {
            repeat(sandBricks.size) { i ->
                put(i, mutableListOf())
            }
        }

    for (i in sandBricks.indices) {
        for (j in 0..<i) {
            if (sandBricks[i].isCollideOn2D(sandBricks[j]) && sandBricks[i].zRange.first == sandBricks[j].zRange.last + 1) {
                supporterBricks[i]!!.add(j)
                bricksToSupport[j]!!.add(i)
            }
        }
    }

    var fallingBricksCount = 0
    for (i in sandBricks.indices) {
        val affectedBricks = bricksToSupport[i]!!.filter { supporterBricks[it]!!.size == 1 }.toMutableList()
        val fallingBricks = affectedBricks.toMutableList()
        fallingBricks.add(i)

        while(affectedBricks.isNotEmpty()) {
            val brick = affectedBricks.removeAt(0)
            for (aboveBrick in bricksToSupport[brick]!!.filter { it !in fallingBricks }) {
                if (fallingBricks.containsAll(supporterBricks[aboveBrick]!!)) {
                    affectedBricks.add(aboveBrick)
                    fallingBricks.add(aboveBrick)
                }
            }
        }

        fallingBricksCount += fallingBricks.size - 1
    }

    return fallingBricksCount
}