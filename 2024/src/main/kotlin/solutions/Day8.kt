package adventofcode.solutions

import adventofcode.utils.coordinate.*
import core.AoCDay

object Day8 : AoCDay<List<List<Char>>>(8) {
    override fun transformInput(input: String): List<List<Char>> {
        return input.lineSequence()
            .map { it.toList() }
            .toList()
    }

    override fun partOne(): Number {
        val antennas = findAllAntennas()
        val antiNodeLocations = mutableSetOf<Coordinate2D>()

        for (antenna in antennas) {
            val antennaCoordinates = findAllCoordinatesOfAntenna(antenna)
            for (i in 0..<antennaCoordinates.size - 1) {
                for (j in (i + 1)..<antennaCoordinates.size) {
                    val vector = antennaCoordinates[i] - antennaCoordinates[j]
                    val antiNode1 = antennaCoordinates[i] + vector
                    val antiNode2 = antennaCoordinates[j] - vector

                    if (antiNode1.isInside(input[0].size, input.size)) {
                        antiNodeLocations.add(antiNode1)
                    }
                    if (antiNode2.isInside(input[0].size, input.size)) {
                        antiNodeLocations.add(antiNode2)
                    }
                }
            }
        }

        return antiNodeLocations.size
    }

    override fun partTwo(): Number {
        val antennas = findAllAntennas()
        val antiNodeLocations = mutableSetOf<Coordinate2D>()

        for (antenna in antennas) {
            val antennaCoordinates = findAllCoordinatesOfAntenna(antenna)
            for (i in 0..<antennaCoordinates.size - 1) {
                for (j in (i + 1)..<antennaCoordinates.size) {
                    val vector = antennaCoordinates[i] - antennaCoordinates[j]
                    var antiNode = antennaCoordinates[i]
                    while (true) {
                        antiNodeLocations.add(antiNode)
                        antiNode += vector
                        if (!antiNode.isInside(input[0].size, input.size)) {
                            break
                        }
                    }

                    antiNode = antennaCoordinates[j]
                    while (true) {
                        antiNodeLocations.add(antiNode)
                        antiNode -= vector
                        if (!antiNode.isInside(input[0].size, input.size)) {
                            break
                        }
                    }
                }
            }
        }

        return antiNodeLocations.size
    }

    private fun findAllAntennas(): Set<Char> {
        val antennas = mutableSetOf<Char>()
        for (row in input) {
            antennas.addAll(row.toSet())
        }

        antennas.remove('.')
        return antennas
    }

    private fun findAllCoordinatesOfAntenna(antenna: Char): List<Coordinate2D> {
        return input.mapIndexedNotNull { y, row ->
            row.mapIndexedNotNull { x, cell ->
                if (cell == antenna) Coordinate2D(x, y) else null
            }.ifEmpty { null }
        }.flatten()
    }
}