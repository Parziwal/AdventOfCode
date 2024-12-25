package adventofcode.utils.graph.astar

class GraphSearchResult<T>(
    val start: T,
    val end: T?,
    private val result: Map<T, VertexWrapper<T>>,
    private val possiblePaths: Map<T, MutableSet<VertexWrapper<T>>>) {

    fun isPathFound() = end != null
    fun getScore() = end?.let { getScore(it) } ?: throw IllegalStateException("No path found")
    fun getScore(vertex: T) = result[vertex]?.cost ?: throw IllegalStateException("Result for $vertex not available")
    fun getPath() = end?.let { getPath(it, emptyList()) } ?: throw IllegalStateException("No path found")
    fun getPossiblePaths() = end?.let { unpackPossiblePaths(it, possiblePaths).toList() } ?: throw IllegalStateException("No path found")

    private tailrec fun getPath(endVertex: T, pathEnd: List<T>): List<T> {
        val previous = result[endVertex]?.previousVertex

        return if (previous == null) {
            listOf(endVertex) + pathEnd
        } else {
            getPath(previous.vertex, listOf(endVertex) + pathEnd)
        }
    }

    private fun unpackPossiblePaths(end: T, possiblePrevious: Map<T, Set<VertexWrapper<T>>>): Set<T> {
        return buildSet {
            add(end)
            val prev = possiblePrevious[end] ?: emptySet()
            prev.flatMapTo(this) {
                unpackPossiblePaths(it.vertex, possiblePrevious)
            }
        }
    }
}