package adventofcode.solutions

import adventofcode.utils.coordinate.*
import adventofcode.utils.graph.astar.GraphSearchResult
import adventofcode.utils.graph.astar.VertexWrapper
import adventofcode.utils.graph.astar.aStarShortestPath
import core.AoCDay

object Day16 : AoCDay<List<List<Char>>>(16) {
    override fun transformInput(input: String): List<List<Char>> {
        return input.lineSequence()
            .map { it.toList() }
            .toList()
    }

    override fun partOne(): Number {
        val startTile = findTilePosition('S')
        val endTile = findTilePosition('E')

        val searchResult = aStarShortestPath(
            startTile,
            { tile -> tile.x == endTile.x && tile.y == endTile.y },
            { tile -> getTileNeighbours(tile) },
            { currTile, nextTile ->
                if (currTile.direction == nextTile.direction) 1 else 1001
            }
        )

        return searchResult.getScore()
    }

    override fun partTwo(): Number {
        val startTile = findTilePosition('S')
        val endTile = findTilePosition('E')

        val searchResult = aStarShortestPath(
            startTile,
            { tile -> tile.x == endTile.x && tile.y == endTile.y },
            { tile -> getTileNeighbours(tile) },
            { currTile, nextTile ->
                if (currTile.direction == nextTile.direction) 1 else 1001
            }
        )

        return searchResult.getPossiblePaths()
            .distinctBy { "x=${it.x}y=${it.y}" }
            .size
    }

    private fun getTileNeighbours(tile: Coordinate2D): List<Coordinate2D> {
        val possibleNeighbours = listOf(
            tile + Direction.Up,
            tile + Direction.Down,
            tile + Direction.Left,
            tile + Direction.Right
        )

        return possibleNeighbours.filter {
            input[it] != '#'
        }
    }

    private fun findTilePosition(tile: Char): Coordinate2D {
        return input.mapIndexedNotNull { y, row ->
            val x = row.indexOf(tile)
            if (x != -1) {
                return@mapIndexedNotNull Coordinate2D(x, y)
            }
            return@mapIndexedNotNull null
        }.first()
    }
}