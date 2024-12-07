package adventofcode.solutions

import adventofcode.utils.coordinate.*
import core.AoCDay

object Day6 : AoCDay<List<MutableList<Char>>>(6) {
    override fun transformInput(input: String): List<MutableList<Char>> {
        return input
            .lineSequence()
            .map { it.toMutableList() }
            .toList()
    }

    override fun partOne(): Number {
        var guardPosition = getGuardStartPosition(input)
        input[guardPosition] = 'X'

        while (true) {
            val nextPosition = guardPosition.nextCoordinates()
            if (nextPosition.x < 0 || nextPosition.x >= input[0].size || nextPosition.y < 0 || nextPosition.y >= input.size) {
                break
            }

            if (input[nextPosition] == '#') {
                guardPosition = guardPosition.copy(direction = guardPosition.direction.rotateRight)
            } else {
                input[nextPosition] = 'X'
                guardPosition = nextPosition
            }
        }

        return input.sumOf { row ->
            row.count { it == 'X' }
        }
    }

    override fun partTwo(): Number {
        val guardStartPosition = getGuardStartPosition(input)
        var guardStuckCount = 0

        for (i in input.indices) {
            for (j in input[0].indices) {
                if ((guardStartPosition.y == i && guardStartPosition.x == j) ||
                    input[i][j] == '#') {
                    continue
                }

                val mapOfLabCopy = input
                    .map { it.toMutableList() }
                    .toMutableList()
                var guardPosition = guardStartPosition.copy()
                mapOfLabCopy[i][j] = '#'
                while (true) {
                    val nextPosition = guardPosition.nextCoordinates()
                    if (nextPosition.x < 0 || nextPosition.x >= mapOfLabCopy[0].size || nextPosition.y < 0 || nextPosition.y >= mapOfLabCopy.size) {
                        break
                    }

                    when {
                        mapOfLabCopy[nextPosition] == '^' && nextPosition.direction == Direction.Up-> {
                            guardStuckCount++
                            break
                        }
                        mapOfLabCopy[nextPosition] == '|' && nextPosition.direction == Direction.Down-> {
                            guardStuckCount++
                            break
                        }
                        mapOfLabCopy[nextPosition] == '>' && nextPosition.direction == Direction.Right-> {
                            guardStuckCount++
                            break
                        }
                        mapOfLabCopy[nextPosition] == '<' && nextPosition.direction == Direction.Left-> {
                            guardStuckCount++
                            break
                        }
                        mapOfLabCopy[nextPosition] == '#' -> {
                            guardPosition = guardPosition.copy(direction = guardPosition.direction.rotateRight)
                        }
                        else -> {
                            mapOfLabCopy[nextPosition] = when(guardPosition.direction) {
                                Direction.Up -> '^'
                                Direction.Down -> '|'
                                Direction.Left -> '<'
                                Direction.Right -> '>'
                            }
                            guardPosition = nextPosition
                        }
                    }
                }
            }
        }

        return guardStuckCount
    }

    private fun getGuardStartPosition(mapOfLab: List<MutableList<Char>>): Coordinate2D {
        for ((rowNumber, row) in mapOfLab.withIndex()) {
            val columnNumber = row.indexOf('^')
            if (columnNumber != -1) {
                return Coordinate2D(columnNumber, rowNumber, Direction.Up)
            }
        }

        throw Error("Guard not found")
    }
}
