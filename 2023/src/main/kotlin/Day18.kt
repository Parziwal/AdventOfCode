import java.io.File
import kotlin.math.absoluteValue

fun main() {
    println("Day 18")
    println("-----------------------------------")
    println("Part One: ${partOneOfDay18()}")
    println("Part Two: ${partTwoOfDay18()}")
    println("-----------------------------------")
}

data class HolePoint(val x: Long, val y: Long)

// https://en.wikipedia.org/wiki/Shoelace_formula
// https://en.wikipedia.org/wiki/Pick%27s_theorem
data class DigPlanPolygon(val points: List<HolePoint>) {
    fun areaWithBoundary(): Long {
        val area = points.zipWithNext()
            .sumOf { (a, b) -> (a.y + b.y) * (a.x - b.x) }
            .absoluteValue
        val perimeter = points.zipWithNext()
            .sumOf { (a, b) -> (a.x - b.x + a.y - b.y).absoluteValue }
        return (area + perimeter) / 2 + 1
    }
}

fun partOneOfDay18(): Long {
    var previousPoint = HolePoint(0, 0)
    val points = mutableListOf(previousPoint)

    File("src/main/resources/day18.txt")
        .forEachLine { line ->
            val direction = line.split(' ')[0]
            val meters = line.split(' ')[1].toInt()

            val point = when(direction) {
                "U" -> HolePoint(previousPoint.x, previousPoint.y - meters)
                "D" -> HolePoint(previousPoint.x, previousPoint.y + meters)
                "L" -> HolePoint(previousPoint.x - meters, previousPoint.y)
                "R" -> HolePoint(previousPoint.x + meters, previousPoint.y)
                else -> throw Exception()
            }
            points.add(point)
            previousPoint = point
        }

    return DigPlanPolygon(points).areaWithBoundary()
}

fun partTwoOfDay18(): Long {
    var previousPoint = HolePoint(0, 0)
    val points = mutableListOf(previousPoint)

    File("src/main/resources/day18.txt")
        .forEachLine { line ->
            val hexCode = line.split(' ')[2].trim('(', ')', '#')
            val hexMeter = hexCode.substring(0..4).toLong(16)

            val point = when(hexCode[5]) {
                '0' -> HolePoint(previousPoint.x + hexMeter, previousPoint.y)
                '1' -> HolePoint(previousPoint.x, previousPoint.y + hexMeter)
                '2' -> HolePoint(previousPoint.x - hexMeter, previousPoint.y)
                '3' -> HolePoint(previousPoint.x, previousPoint.y - hexMeter)
                else -> throw Exception()
            }
            points.add(point)
            previousPoint = point
        }

    return DigPlanPolygon(points).areaWithBoundary()
}