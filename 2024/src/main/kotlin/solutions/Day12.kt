package adventofcode.solutions

import adventofcode.utils.coordinate.*
import core.AoCDay

object Day12 : AoCDay<List<List<Char>>>(12) {
    override fun transformInput(input: String): List<List<Char>> {
        return input.lineSequence()
            .map { it.toList() }
            .toList()
    }

    override fun partOne(): Number {
        return findAllGardenPlots()
            .sumOf { it.area * it.perimeter }
    }

    override fun partTwo(): Number {
        return findAllGardenPlots()
            .sumOf { it.area * it.corners }
    }

    private fun findAllGardenPlots(): List<GardenRegion> {
        val visited = MutableList(input.size) {
            MutableList(input[0].size) {
                false
            }
        }
        val regions = mutableListOf<GardenRegion>()

        for (y in input.indices) {
            for (x in 0..<input[0].size) {
                val currentCoordinate = Coordinate2D(x, y)
                if (!visited[currentCoordinate]) {
                    val region = calculateGardenPlotRegion(currentCoordinate, GardenRegion(0, 0, 0), visited)
                    regions.add(region)
                }
            }
        }

        return regions
    }

    private fun calculateGardenPlotRegion(currentCoordinate: Coordinate2D, region: GardenRegion, visited: List<MutableList<Boolean>>): GardenRegion {
        if (visited[currentCoordinate]) {
            return region
        }

        visited[currentCoordinate] = true
        val sameNeighbourPlants = getSameNeighbourPlants(currentCoordinate)

        if (sameNeighbourPlants.isEmpty()) {
            region.area = 1
            region.perimeter = 4
            region.corners = 4
            return region
        }

        region.area += 1
        region.perimeter += 4 - sameNeighbourPlants.size
        region.corners += getCorners(currentCoordinate)

        for (neighbour in sameNeighbourPlants) {
            calculateGardenPlotRegion(neighbour, region, visited)
        }

        return region
    }

    private fun getSameNeighbourPlants(currentCoordinate: Coordinate2D): List<Coordinate2D> {
        val directions = Direction.entries.filter { it != Direction.None }
        val plant = input[currentCoordinate]
        val sameNeighbourPlants = mutableListOf<Coordinate2D>()

        for (dir in directions) {
            if (input.getOrNull(currentCoordinate + dir) == plant) {
                sameNeighbourPlants.add(currentCoordinate + dir)
            }
        }

        return sameNeighbourPlants
    }

    private fun getCorners(currentCoordinate: Coordinate2D): Int {
        val plant = input[currentCoordinate]
        var corners = 0

        val topCell = input.getOrNull(currentCoordinate + Direction.Up)
        val bottomCell = input.getOrNull(currentCoordinate + Direction.Down)
        val leftCell = input.getOrNull(currentCoordinate + Direction.Left)
        val rightCell = input.getOrNull(currentCoordinate + Direction.Right)

        // Outside Top Left corner
        if ((topCell == null || topCell != plant) && (leftCell == null || leftCell != plant)) {
            corners++
        }
        // Outside Top Right corner
        if ((topCell == null || topCell != plant) && (rightCell == null || rightCell != plant)) {
            corners++
        }
        // Outside Bottom Left corner
        if ((bottomCell == null || bottomCell != plant) && (leftCell == null || leftCell != plant)) {
            corners++
        }
        // Outside Bottom Right corner
        if ((bottomCell == null || bottomCell != plant) && (rightCell == null || rightCell != plant)) {
            corners++
        }

        val topLeftCell = input.getOrNull(currentCoordinate + Direction.Up + Direction.Left)
        val topRightCell = input.getOrNull(currentCoordinate + Direction.Up + Direction.Right)
        val bottomLeftCell = input.getOrNull(currentCoordinate + Direction.Down + Direction.Left)
        val bottomRightCell = input.getOrNull(currentCoordinate + Direction.Down + Direction.Right)

        // Inside Top Left corner
        if (bottomRightCell != null && bottomRightCell != plant && bottomCell == plant && rightCell == plant) {
            corners++
        }
        // Inside Top Right corner
        if (bottomLeftCell != null && bottomLeftCell != plant && bottomCell == plant && leftCell == plant) {
            corners++
        }
        // Inside Bottom Left corner
        if (topRightCell != null && topRightCell != plant && topCell == plant && rightCell == plant) {
            corners++
        }
        // Inside Bottom Right corner
        if (topLeftCell != null && topLeftCell != plant && topCell == plant && leftCell == plant) {
            corners++
        }

        return corners
    }

    data class GardenRegion(var area: Int, var perimeter: Int, var corners: Int)
}