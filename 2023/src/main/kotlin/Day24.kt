import java.io.File

fun main() {
    println("Day 24")
    println("-----------------------------------")
    println("Part One: ${partOneOfDay24()}")
    println("Part Two: -")
    println("-----------------------------------")
}

data class HailStone(
    val sx: Double,
    val sy: Double,
    val sz: Double,
    val vx: Double,
    val vy: Double,
    val vz: Double,
) {
    val a = vy
    val b = -vx
    val c = vy * sx - vx * sy
}

fun partOneOfDay24(): Int {
    val hailStones = File("src/main/resources/day24.txt")
        .readLines()
        .map { line ->
            val data = line.split(',', '@')
                .map { it.trim().toDouble() }

            HailStone(data[0], data[1], data[2], data[3], data[4], data[5])
        }

    var total = 0
    for ((i, hs1) in hailStones.withIndex()) {
        for (hs2 in hailStones.subList(0, i)) {
            if (hs1.a * hs2.b == hs2.a * hs1.b) {
                continue
            }

            val x = (hs1.c * hs2.b - hs2.c * hs1.b) / (hs1.a * hs2.b - hs2.a * hs1.b)
            val y = (hs2.c * hs1.a - hs1.c * hs2.a) / (hs1.a * hs2.b - hs2.a * hs1.b)

            if (x in 200_000_000_000_000.0..400_000_000_000_000.0 && y in 200_000_000_000_000.0..400_000_000_000_000.0) {
                if (listOf(hs1, hs2).all { (x - it.sx) * it.vx >= 0 && (y - it.sy) * it.vy >= 0 }) {
                    total++
                }
            }
        }
    }

    return total
}