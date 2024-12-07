package adventofcode.solutions

import adventofcode.utils.coordinate.*
import core.AoCDay

class Day6 : AoCDay(6) {
    override fun partOne(input: String): Number {
        val mapOfLab = readInput(input)
        var guardPosition = getGuardStartPosition(mapOfLab)
        mapOfLab[guardPosition] = 'X'

        while (true) {
            val nextPosition = guardPosition.nextCoordinates()
            if (nextPosition.x < 0 || nextPosition.x >= mapOfLab[0].size || nextPosition.y < 0 || nextPosition.y >= mapOfLab.size) {
                break
            }

            if (mapOfLab[nextPosition] == '#') {
                guardPosition = guardPosition.copy(direction = guardPosition.direction.rotateRight)
            } else {
                mapOfLab[nextPosition] = 'X'
                guardPosition = nextPosition
            }
        }

        return mapOfLab.sumOf { row ->
            row.count { it == 'X' }
        }
    }

    override fun partTwo(input: String): Number {
        val mapOfLab = readInput(input)
        val guardStartPosition = getGuardStartPosition(mapOfLab)
        var guardStuckCount = 0

        for (i in mapOfLab.indices) {
            for (j in mapOfLab[0].indices) {
                if ((guardStartPosition.y == i && guardStartPosition.x == j) ||
                    mapOfLab[i][j] == '#') {
                    continue
                }

                val mapOfLabCopy = mapOfLab
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

    private fun readInput(input: String): List<MutableList<Char>> {
        return input
            .lineSequence()
            .map { it.toMutableList() }
            .toList()
    }
}
