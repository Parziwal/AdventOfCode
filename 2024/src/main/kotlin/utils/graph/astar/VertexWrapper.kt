package adventofcode.utils.graph.astar

data class VertexWrapper<T>(
    val vertex: T,
    var cost: Int,
    val heuristic: Int,
    var previousVertex: VertexWrapper<T>? = null) : Comparable<VertexWrapper<T>> {
    override fun compareTo(other: VertexWrapper<T>): Int = (cost + heuristic).compareTo(other.cost + other.heuristic)
}