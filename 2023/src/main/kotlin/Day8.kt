import java.io.File

fun main() {
    println("Day 8")
    println("-----------------------------------")
    println("Part One: ${partOneOfDay8()}")
    println("Part Two: ${partTwoOfDay8()}")
    println("-----------------------------------")
}

fun Long.gcd(other: Long): Long {
    var a = this
    var b = other
    while (b > 0) {
        val temp = b
        b = a % b
        a = temp
    }
    return a
}

fun Long.lcm(other: Long): Long {
    return this * (other / this.gcd(other))
}


fun partOneOfDay8(): Int {
    val lines = File("src/main/resources/day8.txt")
        .readLines()

    val directions = lines.first()

    val nodes = lines.drop(2).associate { line ->
        val mainNode = line.split('=')[0]
            .trim()
        val connectedNodes = line.split('=')[1]
            .split(',')
            .map { it.trim(' ', '(', ')') }

        mainNode to Pair(connectedNodes[0], connectedNodes[1])
    }

    var currentNode = "AAA"

    var steps = 0
    while (currentNode != "ZZZ") {
        val currentDirection = directions[steps % directions.length]

        currentNode = when(currentDirection) {
            'L' -> nodes[currentNode]!!.first
            'R' -> nodes[currentNode]!!.second
            else -> ""
        }
        steps++
    }

    return steps
}

fun partTwoOfDay8(): Long {
    val lines = File("src/main/resources/day8.txt")
        .readLines()

    val directions = lines.first()

    val nodes = lines.drop(2).associate { line ->
        val mainNode = line.split('=')[0]
            .trim()
        val connectedNodes = line.split('=')[1]
            .split(',')
            .map { it.trim(' ', '(', ')') }

        mainNode to Pair(connectedNodes[0], connectedNodes[1])
    }

    val currentNodes = nodes.keys.filter {
        it.endsWith('A')
    }.toMutableList()

    val minStepsForEachNode = List(currentNodes.size) { _ -> 0}
        .toMutableList()
    while (currentNodes.any { !it.endsWith('Z') }) {
        for (i in currentNodes.indices) {
            val currentDirection = directions[minStepsForEachNode[i] % directions.length]
            if (!currentNodes[i].endsWith('Z')) {
                currentNodes[i] = when(currentDirection) {
                    'L' -> nodes[currentNodes[i]]!!.first
                    'R' -> nodes[currentNodes[i]]!!.second
                    else -> ""
                }
                minStepsForEachNode[i]++
            }
        }
    }

    var minSteps = minStepsForEachNode[0].toLong()
    for (i in 1..<minStepsForEachNode.size) {
        minSteps = minSteps.lcm(minStepsForEachNode[i].toLong())
    }

    return minSteps
}