package adventofcode.solutions

import adventofcode.utils.coordinate.*
import adventofcode.utils.graph.astar.aStarShortestPath
import core.AoCDay

object Day18 : AoCDay<List<Coordinate2D>>(18) {
    private val memoryDimension = Coordinate2D(71, 71)

    override fun transformInput(input: String): List<Coordinate2D> {
        return input.lineSequence()
            .map { line ->
                val (x, y) = line.split(",")
                    .map { it.toInt() }
                Coordinate2D(x, y)
            }.toList()
    }

    override fun partOne(): Number {
        val searchResult = aStarShortestPath(
            Coordinate2D(0, 0),
            { field -> field.x == memoryDimension.x - 1 && field.y == memoryDimension.y - 1 },
            { field -> findSafeNeighbourFields(field, input.take(1024)) },
        )

        return searchResult.getPath().size - 1
    }

    override fun partTwo(): Number {
        var isPathFound = false
        var byteIndex = input.size
        while (!isPathFound) {
            val searchResult = aStarShortestPath(
                Coordinate2D(0, 0),
                { field -> field.x == memoryDimension.x - 1 && field.y == memoryDimension.y - 1 },
                { field -> findSafeNeighbourFields(field, input.take(byteIndex))},
            )
            isPathFound = searchResult.isPathFound()
            byteIndex--
        }

        val firstBytePreventExit = input[byteIndex + 1]
        println("Day 18 |> Part Two: ${firstBytePreventExit.x},${firstBytePreventExit.y}")
        return 0
    }

    private fun findSafeNeighbourFields(field: Coordinate2D, fallenBytesCoordinates: List<Coordinate2D>): List<Coordinate2D> {
        return listOf(Direction.Up, Direction.Down, Direction.Left, Direction.Right).mapNotNull { dir ->
            val neighbourField = (field + dir).copy(direction = Direction.None)
            if (neighbourField.isInside(memoryDimension - 1)
                && !fallenBytesCoordinates.contains(neighbourField)
            ) neighbourField else null
        }
    }
}