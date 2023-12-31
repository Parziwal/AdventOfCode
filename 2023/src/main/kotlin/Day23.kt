import java.io.File

fun main() {
    println("Day 23")
    println("-----------------------------------")
    println("Part One: ${partOneOfDay23()}")
    println("Part Two: ${partTwoOfDay23()}")
    println("-----------------------------------")
}

data class WalkPoint(val x: Int, val y: Int)

fun WalkPoint.neighbourFields(map: List<String>, part1: Boolean): List<WalkPoint> {
    return when {
        map[this.y][this.x] == '.' || !part1 -> listOf(
            WalkPoint(this.x - 1, this.y),
            WalkPoint(this.x + 1, this.y),
            WalkPoint(this.x, this.y - 1),
            WalkPoint(this.x, this.y + 1))
        map[this.y][this.x] == '<' -> listOf(WalkPoint(this.x - 1, this.y))
        map[this.y][this.x] == '>' -> listOf(WalkPoint(this.x + 1, this.y))
        map[this.y][this.x] == '^' -> listOf(WalkPoint(this.x, this.y - 1))
        map[this.y][this.x] == 'v' -> listOf(WalkPoint(this.x, this.y + 1))
        else -> throw Exception()
    }.filter { it.x >= 0 && it.y >= 0 && it.x < map.first().length && it.y < map.size && map[it.y][it.x] != '#' }
}

fun WalkPoint.getNextIntersection(
    map: List<String>,
    previous: WalkPoint,
    end: WalkPoint,
    part1: Boolean): Pair<WalkPoint, Int>? {
    val path = mutableListOf(previous)
    var current = this
    var neighbours = current.neighbourFields(map, part1).filter { it !in path }
    while (neighbours.size == 1) {
        path.add(current)
        current = neighbours.first()
        neighbours = current.neighbourFields(map, part1).filter { it !in path }
    }

    return if (neighbours.isNotEmpty() || current == end) {
        current to path.size
    } else {
        null
    }
}

fun buildWalkPath(
    map: List<String>,
    current: WalkPoint,
    end: WalkPoint,
    walkPath: MutableMap<WalkPoint, MutableMap<WalkPoint, Int>>,
    part1: Boolean) {
    val currentNeighbours = current.neighbourFields(map, part1)
    val paths = currentNeighbours.mapNotNull {
        it.getNextIntersection(map, current, end, part1)
    }
    walkPath[current] = paths.toMap(mutableMapOf())

    paths.forEach { (point, _) ->
        if (point !in walkPath) {
            buildWalkPath(map, point, end, walkPath, part1)
        }
    }
}

fun partOneOfDay23(): Int {
    val hikeMap = File("src/main/resources/day23.txt")
        .readLines()
    val start = WalkPoint(hikeMap[0].indexOf('.'), 0)
    val end = WalkPoint(hikeMap.last().indexOf('.'), hikeMap.lastIndex)
    val walkPath = mutableMapOf<WalkPoint, MutableMap<WalkPoint, Int>>()

    buildWalkPath(hikeMap, start, end, walkPath, true)

    return longestPathDfs(walkPath, start, end)
}

fun partTwoOfDay23(): Int {
    val hikeMap = File("src/main/resources/day23.txt")
        .readLines()
    val start = WalkPoint(hikeMap[0].indexOf('.'), 0)
    val end = WalkPoint(hikeMap.last().indexOf('.'), hikeMap.lastIndex)
    val walkPath = mutableMapOf<WalkPoint, MutableMap<WalkPoint, Int>>()

    buildWalkPath(hikeMap, start, end, walkPath, false)

    return longestPathDfs(walkPath, start, end)
}

fun <T> longestPathDfs(
    graph: Map<T, Map<T, Int>>,
    current: T,
    end: T,
    seen: MutableSet<T> = mutableSetOf()
): Int {
    if (current == end) {
        return 0
    }

    seen.add(current)
    val max = graph[current]!!.maxOfOrNull { (next, cost) ->
        if (next !in seen) {
            longestPathDfs(graph, next, end, seen) + cost
        } else {
            Int.MIN_VALUE
        }
    } ?: Int.MIN_VALUE
    seen.remove(current)
    return max
}