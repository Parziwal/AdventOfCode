package adventofcode.solutions

import adventofcode.utils.coordinate.*
import core.AoCDay

object Day14 : AoCDay<List<Day14.Robot>>(14) {
    private val areaSize = Coordinate2D(101, 103)

    override fun transformInput(input: String): List<Robot> {
        return input.lineSequence()
            .map { line ->
                val position = line.substringAfter("p=")
                    .substringBefore(" ")
                    .split(",")
                    .map { it.toInt() }
                val vector = line.substringAfter("v=")
                    .split(",")
                    .map { it.toInt() }

                Robot(Coordinate2D(position[0], position[1]), Coordinate2D(vector[0], vector[1]))
            }.toList()
    }

    override fun partOne(): Number {
        var robotsState = input.toList()

        repeat(100) {
            robotsState = robotsState.map {
                val newPosition = (it.position + it.vector + areaSize) % areaSize
                it.copy(position = newPosition)
            }
        }

        val topLeftQuadrant = robotsState.count {
            it.position.isInside(areaSize / 2 - 1)
        }
        val topRightQuadrant = robotsState.count {
            it.position.isInside(areaSize / 2 - 1, Coordinate2D(areaSize.x / 2 + 1, 0))
        }
        val bottomLeftQuadrant = robotsState.count {
            it.position.isInside(areaSize / 2 - 1, Coordinate2D(0, areaSize.y / 2 + 1))
        }
        val bottomRightQuadrant = robotsState.count {
            it.position.isInside(areaSize / 2 - 1, Coordinate2D(areaSize.x / 2 + 1, areaSize.y / 2 + 1))
        }

        return topLeftQuadrant * topRightQuadrant * bottomLeftQuadrant * bottomRightQuadrant
    }

    override fun partTwo(): Number {
        var robotsState = input.toList()
        var seconds = 0

        while(robotsState.distinctBy { it.position }.size != robotsState.size) {
            robotsState = robotsState.map {
                val newPosition = (it.position + it.vector + areaSize) % areaSize
                it.copy(position = newPosition)
            }

            seconds++
        }

        printMap(robotsState)

        return seconds
    }

    private fun printMap(robotsState: List<Robot>) {
        repeat(areaSize.y) { y ->
            var row = ("." +
                    "").repeat(areaSize.x)
            robotsState.filter { it.position.y == y }
                .groupBy { it.position.x }
                .forEach {
                    row = row.replaceRange(it.key..it.key, it.value.size.toString())
                }
            robotsState.distinctBy {  }
            println(row)
        }
    }

    data class Robot(val position: Coordinate2D, val vector: Coordinate2D)
}