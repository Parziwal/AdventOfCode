package adventofcode.solutions

import adventofcode.utils.coordinate.*
import core.AoCDay

object Day10 : AoCDay<List<List<Int>>>(10) {
    override fun transformInput(input: String): List<List<Int>> {
        return input
            .lineSequence()
            .map { line ->
                line.map { it.digitToInt() }
                    .toList()
            }
            .toList()
    }

    override fun partOne(): Number {
        var sumOfScoresOfTrailheads = 0
        for ((y, row) in input.withIndex()) {
            for ((x, cell) in row.withIndex()) {
                if (cell == 0) {
                    sumOfScoresOfTrailheads += countReachable9HeightPositions(Coordinate2D(x, y))
                }
            }
        }

        return sumOfScoresOfTrailheads
    }

    override fun partTwo(): Number {
        var sumOfRatingsOfTrailheads = 0
        for ((y, row) in input.withIndex()) {
            for ((x, cell) in row.withIndex()) {
                if (cell == 0) {
                    sumOfRatingsOfTrailheads += count9HeightDifferentPaths(Coordinate2D(x, y))
                }
            }
        }

        return sumOfRatingsOfTrailheads
    }

    private fun countReachable9HeightPositions(coordinate: Coordinate2D, visited9Heights: MutableSet<Coordinate2D> = mutableSetOf()): Int {
        if (visited9Heights.contains(coordinate.copy(direction = Direction.None))) {
            return 0
        }

        val topCell = coordinate + Coordinate2D(0, -1, Direction.Up)
        val bottomCell = coordinate + Coordinate2D(0, 1, Direction.Down)
        val leftCell = coordinate + Coordinate2D(-1, 0, Direction.Left)
        val rightCell = coordinate + Coordinate2D(1, 0, Direction.Right)

        if (coordinate.direction != Direction.None) {
            if (!coordinate.isInside(Coordinate2D(input[0].size - 1, input.size - 1))
                || (coordinate.direction == Direction.Up && input[coordinate] - 1 != input[bottomCell])
                || (coordinate.direction == Direction.Down && input[coordinate] - 1 != input[topCell])
                || (coordinate.direction == Direction.Left && input[coordinate] - 1 != input[rightCell])
                || (coordinate.direction == Direction.Right && input[coordinate] - 1 != input[leftCell])) {
                return 0
            }
        }

        if (input[coordinate] == 9) {
            visited9Heights.add(coordinate.copy(direction = Direction.None))
            return 1
        }

        return countReachable9HeightPositions(topCell, visited9Heights) + countReachable9HeightPositions(bottomCell, visited9Heights) +
                countReachable9HeightPositions(leftCell, visited9Heights) + countReachable9HeightPositions(rightCell, visited9Heights)
    }

    private fun count9HeightDifferentPaths(coordinate: Coordinate2D): Int {
        val topCell = coordinate + Coordinate2D(0, -1, Direction.Up)
        val bottomCell = coordinate + Coordinate2D(0, 1, Direction.Down)
        val leftCell = coordinate + Coordinate2D(-1, 0, Direction.Left)
        val rightCell = coordinate + Coordinate2D(1, 0, Direction.Right)

        if (coordinate.direction != Direction.None) {
            if (!coordinate.isInside(Coordinate2D(input[0].size - 1, input.size - 1))
                || (coordinate.direction == Direction.Up && input[coordinate] - 1 != input[bottomCell])
                || (coordinate.direction == Direction.Down && input[coordinate] - 1 != input[topCell])
                || (coordinate.direction == Direction.Left && input[coordinate] - 1 != input[rightCell])
                || (coordinate.direction == Direction.Right && input[coordinate] - 1 != input[leftCell])) {
                return 0
            }
        }

        if (input[coordinate] == 9) {
            return 1
        }

        return count9HeightDifferentPaths(topCell) + count9HeightDifferentPaths(bottomCell) +
                count9HeightDifferentPaths(leftCell) + count9HeightDifferentPaths(rightCell)
    }
}