package adventofcode.solutions

import core.AoCDay

object Day13 : AoCDay<List<Day13.LinearEquation>>(13) {
    override fun transformInput(input: String): List<LinearEquation> {
        return input.lineSequence()
            .chunked(4)
            .map { lines ->
                val buttonA = lines[0]
                    .replace(Regex("[^0-9|,]"), "")
                    .split(",")
                    .map { it.toLong() }
                val buttonB = lines[1]
                    .replace(Regex("[^0-9|,]"), "")
                    .split(",")
                    .map { it.toLong() }
                val prize = lines[2]
                    .replace(Regex("[^0-9|,]"), "")
                    .split(",")
                    .map { it.toLong() }

                LinearEquation(
                    buttonA[0], buttonA[1],
                    buttonB[0], buttonB[1],
                    prize[0], prize[1]
                )
            }.toList()
    }

    override fun partOne(): Number {
        return input.sumOf {
            val solution = it.solve()
            solution[0] * 3 + solution[1]
        }
    }

    override fun partTwo(): Number {
         val linearEquations = input.map {
             val prizeX = 10000000000000L + it.prizeX
             val prizeY = 10000000000000L + it.prizeY
            it.copy(prizeX = prizeX, prizeY = prizeY)
        }

        return linearEquations.sumOf {
            val solution = it.solve()
            solution[0] * 3 + solution[1]
        }
    }

    data class LinearEquation(
        val aX: Long, val aY: Long,
        val bX: Long, val bY: Long,
        val prizeX: Long, val prizeY: Long,
    ) {
        fun solve(): List<Long> {
            // Cramer's rule
            // A * X = B
            // a = det(A) / det(A1)
            // b = det(A) / det(A2)
            // where Ai is the matrix obtained by replacing the i. column of A with the B

            val detA = aX * bY - bX * aY
            val a = (prizeX * bY - bX * prizeY) / detA
            val b = (aX * prizeY - prizeX * aY) / detA

            return if (a * aX + b * bX == prizeX && a * aY + b * bY == prizeY) listOf(a, b) else listOf(0, 0)
        }
    }
}