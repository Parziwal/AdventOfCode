package adventofcode.utils.graph.astar

import java.util.*

/*
    Sources:
    https://yuminlee2.medium.com/a-search-algorithm-42c1a13fcf9f
    https://github.com/Mistborn94/advent-of-code-2023/blob/master/src/main/kotlin/day17/Day17.kt
 */

typealias HeuristicFunction<T> = (T) -> Int
typealias NeighbourFunction<T> = (T) -> Iterable<T>
typealias CostFunction<T> = (T, T) -> Int

/**
 * Dijkstra's algorithm to get the shortest path from a starting vertex to all reachable vertices
 */
fun <T> shortestPathToAll(
    start: T,
    neighbours: NeighbourFunction<T>,
    cost: CostFunction<T> = { _, _ -> 1 }
): GraphSearchResult<T> = aStarShortestPath(start, { false }, neighbours, cost)

/**
 * A* search to find the shortest path between two vertices
 */
fun <T>aStarShortestPath(
    startVertex: T,
    endPredicate: (T) -> Boolean,
    neighbours: NeighbourFunction<T>,
    cost: CostFunction<T> = { _, _ -> 1 },
    heuristic: HeuristicFunction<T> = { 0 },
): GraphSearchResult<T> {
    val openList = PriorityQueue(listOf(VertexWrapper(startVertex, 0, heuristic(startVertex))))
    val closeList = mutableMapOf<T, VertexWrapper<T>>()
    var endVertex: VertexWrapper<T>? = null
    val possiblePaths = mutableMapOf<T, MutableSet<VertexWrapper<T>>>()

    while (openList.isNotEmpty()) {
        val currentVertex = openList.remove()
        if (endPredicate(currentVertex.vertex)) {
            closeList[currentVertex.vertex] = currentVertex
            endVertex = currentVertex
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
                if (openVertex.cost == neighbourCost) {
                    possiblePaths[neighbourVertex]!!.add(currentVertex)
                }
                continue
            }

            possiblePaths[neighbourVertex] = mutableSetOf(currentVertex)

            if (openVertex != null) {
                openList.remove(openVertex)
            }

            val updatedVertex = VertexWrapper(
                neighbourVertex,
                neighbourCost,
                heuristic(neighbourVertex),
                currentVertex
            )
            openList.add(updatedVertex)
        }

        closeList[currentVertex.vertex] = currentVertex
    }

    return GraphSearchResult(startVertex, endVertex?.vertex, closeList, possiblePaths)
}