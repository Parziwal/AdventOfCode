package adventofcode.solutions

import adventofcode.utils.coordinate.*
import adventofcode.utils.graph.astar.shortestPathToAll
import core.AoCDay

object Day20 : AoCDay<List<List<Char>>>(20) {
    override fun transformInput(input: String): List<List<Char>> {
        return input.lineSequence()
            .map { it.toList() }
            .toList()
    }

    override fun partOne(): Number {
        return countCheats(2)
    }

    override fun partTwo(): Number {
        return countCheats(20)
    }

    private fun countCheats(cheatLength: Int, minTimeSave: Int = 100): Int {
        val startCoordinate = findFieldCoordinate('S')
        val endCoordinate = findFieldCoordinate('E')

        val searchResult = shortestPathToAll(
            startCoordinate,
            { field -> findNeighbourFields(field) },
        )
        val endPath = searchResult.getPath(endCoordinate)

        return endPath.withIndex().sumOf { (i, cheatStart) ->
            endPath.subList(i + 1, endPath.size).count { cheatEnd ->
                val distance = (cheatStart - cheatEnd).abs()
                distance <= cheatLength && searchResult.getScore(cheatEnd) - i - distance >= minTimeSave
            }
        }
    }

    private fun findNeighbourFields(field: Coordinate2D): List<Coordinate2D> {
        return listOf(Direction.Up, Direction.Down, Direction.Left, Direction.Right).mapNotNull { dir ->
            val neighbourField = (field + dir).copy(direction = Direction.None)
            if (input[neighbourField] != '#') neighbourField else null
        }
    }

    private fun findFieldCoordinate(field: Char): Coordinate2D {
        for ((y, row) in input.withIndex()) {
            val x = row.indexOf(field)
            if (x != -1) {
                return Coordinate2D(x, y)
            }
        }

        throw Exception("Field not found")
    }
}