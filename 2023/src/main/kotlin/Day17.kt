import java.io.File
import java.util.PriorityQueue

fun main() {
    println("Day 17")
    println("-----------------------------------")
    println("Part One: ${partOneOfDay17()}")
    println("Part Two: ${partTwoOfDay17()}")
    println("-----------------------------------")
}

enum class CrucibleDirection {
    Up {
        override val left get() = Left
        override val right get() = Right
        override val opposite get() = Down
    },
    Down {
        override val left get() = Right
        override val right get() = Left
        override val opposite get() = Up
    },
    Left {
        override val left get() = Down
        override val right get() = Up
        override val opposite get() = Right
    },
    Right {
        override val left get() = Up
        override val right get() = Down
        override val opposite get() = Left
    };

    abstract val left: CrucibleDirection
    abstract val right: CrucibleDirection
    abstract val opposite: CrucibleDirection
}

data class CityBlock(val x: Int, val y: Int, val direction: CrucibleDirection, val line: Int) {
    fun neighbours(): List<CityBlock> {
        return buildList {
            if (line < 3) {
                add(nextBlock(direction, line + 1))
            }
            add(nextBlock(direction.left, 1))
            add(nextBlock(direction.right, 1))
        }
    }

    fun ultraNeighbours(): List<CityBlock> {
        return buildList {
            if (line < 10) {
                add(nextBlock(direction, line + 1))
            }
            if (line >= 4 || line == 0) {
                add(nextBlock(direction.left, 1))
                add(nextBlock(direction.right, 1))
            }
        }
    }

    private fun nextBlock(dir: CrucibleDirection, line: Int): CityBlock {
        return when(dir) {
            CrucibleDirection.Up -> CityBlock(x, y - 1, dir, line)
            CrucibleDirection.Down -> CityBlock(x, y + 1, dir, line)
            CrucibleDirection.Left -> CityBlock(x - 1, y, dir, line)
            CrucibleDirection.Right -> CityBlock(x + 1, y, dir, line)
        }
    }
}

fun partOneOfDay17(): Int {
    val heatLossMap = File("src/main/resources/day17.txt")
        .readLines()
        .map { line ->
            line.map { cell ->
                cell.digitToInt()
            }
        }

    val start = CityBlock(0, 0, CrucibleDirection.Right, 0)

    return aStarAlgorithm(
        start,
        { block ->
            block.x == heatLossMap[0].lastIndex && block.y == heatLossMap.lastIndex
        },
        { block ->
            block.neighbours()
                .filter { it.x >= 0 && it.x < heatLossMap[0].size && it.y >= 0 && it.y < heatLossMap.size }
        },
        { _, nextBlock ->
            heatLossMap[nextBlock.y][nextBlock.x]
        }).getScore()
}

fun partTwoOfDay17(): Int {
    val heatLossMap = File("src/main/resources/day17.txt")
        .readLines()
        .map { line ->
            line.map { cell ->
                cell.digitToInt()
            }
        }

    val start = CityBlock(0, 0, CrucibleDirection.Right, 0)

    return aStarAlgorithm(
        start,
        { block ->
            block.x == heatLossMap[0].lastIndex && block.y == heatLossMap.lastIndex && block.line >= 4
        },
        { block ->
            block.ultraNeighbours()
                .filter { it.x >= 0 && it.x < heatLossMap[0].size && it.y >= 0 && it.y < heatLossMap.size }
        },
        { _, nextBlock ->
            heatLossMap[nextBlock.y][nextBlock.x]
        }).getScore()
}

/*
Sources:
https://yuminlee2.medium.com/a-search-algorithm-42c1a13fcf9f
https://github.com/Mistborn94/advent-of-code-2023/blob/master/src/main/kotlin/day17/Day17.kt
 */

typealias HeuristicFunction<T> = (T) -> Int
typealias NeighbourFunction<T> = (T) -> Iterable<T>
typealias CostFunction<T> = (T, T) -> Int

data class VertexWrapper<T>(
    val vertex: T,
    var cost: Int,
    val heuristic: Int,
    var previousVertex: VertexWrapper<T>? = null) : Comparable<VertexWrapper<T>> {
    override fun compareTo(other: VertexWrapper<T>): Int = (cost + heuristic).compareTo(other.cost + other.heuristic)
}

class GraphSearchResult<T>(val start: T, val end: T, private val result: Map<T, VertexWrapper<T>>) {
    fun getScore() = end?.let { getScore(it) } ?: throw IllegalStateException("No path found")
    fun getScore(vertex: T) = result[vertex]?.cost ?: throw IllegalStateException("Result for $vertex not available")
    fun getPath() = end?.let { getPath(it, emptyList()) } ?: throw IllegalStateException("No path found")

    private tailrec fun getPath(endVertex: T, pathEnd: List<T>): List<T> {
        val previous = result[endVertex]?.previousVertex

        return if (previous == null) {
            listOf(endVertex) + pathEnd
        } else {
            getPath(previous.vertex, listOf(endVertex) + pathEnd)
        }
    }
}

fun <T>aStarAlgorithm(
    startVertex: T,
    endPredicate: (T) -> Boolean,
    neighbours: NeighbourFunction<T>,
    cost: CostFunction<T> = { _, _ -> 1 },
    heuristic: HeuristicFunction<T> = { 0 },
    ): GraphSearchResult<T> {
    val openList = PriorityQueue(listOf(VertexWrapper(startVertex, 0, heuristic(startVertex))))
    val closeList = mutableMapOf<T, VertexWrapper<T>>()

    while (openList.isNotEmpty()) {
        val currentVertex = openList.remove()
        if (endPredicate(currentVertex.vertex)) {
            closeList[currentVertex.vertex] = currentVertex
            break
        }

        val neighbourVertexes = neighbours(currentVertex.vertex)

        for (neighbourVertex in neighbourVertexes) {
            if (neighbourVertex in closeList) {
                continue
            }

            val openVertex = openList.find { it.vertex == neighbourVertex }
            val neighbourCost = currentVertex.cost + cost(currentVertex.vertex, neighbourVertex)

            if (openVertex != null && openVertex.cost <= neighbourCost) {
                continue
            }

            val updatedVertex = VertexWrapper(
                neighbourVertex,
                neighbourCost,
                heuristic(neighbourVertex),
                currentVertex
            )

            if (openVertex != null) {
                openList.remove(openVertex)
            }
            openList.add(updatedVertex)
        }

        closeList[currentVertex.vertex] = currentVertex
    }

    return GraphSearchResult(startVertex, closeList.values.last().vertex, closeList)
}