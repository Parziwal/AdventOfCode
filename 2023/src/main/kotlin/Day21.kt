import java.io.File
import java.math.BigDecimal

fun main() {
    println("Day 21")
    println("-----------------------------------")
    println("Part One: ${partOneOfDay21()}")
    println("Part Two: ${partTwoOfDay21()}")
    println("-----------------------------------")
}

tailrec fun nextStepInGarden(gardenMap: List<String>, step: Int): Int {
    val updatedGardenMap = gardenMap.toMutableList()
    for (i in gardenMap.indices) {
        for (j in gardenMap.first().indices) {
            if (gardenMap[i][j] == 'O' || gardenMap[i][j] == 'S') {
                updatedGardenMap[i] = updatedGardenMap[i].replaceRange(j..j, ".")
                if (i > 0 && updatedGardenMap[i - 1][j] == '.')
                    updatedGardenMap[i - 1] = updatedGardenMap[i - 1].replaceRange(j..j, "O")
                if (i < updatedGardenMap.size - 1 && updatedGardenMap[i + 1][j] == '.')
                    updatedGardenMap[i + 1] = updatedGardenMap[i + 1].replaceRange(j..j, "O")
                if (j > 0 && updatedGardenMap[i][j - 1] == '.')
                    updatedGardenMap[i] = updatedGardenMap[i].replaceRange(j - 1..<j, "O")
                if (j < updatedGardenMap.first().length - 1 && updatedGardenMap[i][j + 1] == '.')
                    updatedGardenMap[i] = updatedGardenMap[i].replaceRange(j + 1..j + 1 , "O")
            }
        }
    }

    return if (step > 1) {
        nextStepInGarden(updatedGardenMap, step - 1)
    } else {
        updatedGardenMap.reachableGardenPlotsCount()
    }
}

fun List<String>.reachableGardenPlotsCount(): Int {
    return this.sumOf {
        it.filter { plot -> plot == 'O' }.length
    }
}

fun List<List<Double>>.multiply(vector: List<Int>): List<Double> {
    val result = MutableList(this.size) { 0.0 }

    for (i in indices) {
        for (j in this[0].indices) {
            result[i] += this[i][j] * vector[j]
        }
    }

    return result
}

fun List<String>.repeatMatrix(horizontal: Int = 0, vertical: Int = 0): List<String> {
    val matrix = mutableListOf<String>()
    repeat(vertical + 1) {
        this.forEach {
            matrix.add(it.repeat(horizontal + 1))
        }
    }

    return matrix
}

fun partOneOfDay21(): Int {
    val gardenMap = File("src/main/resources/day21.txt")
        .readLines()
        .toMutableList()

    return nextStepInGarden(gardenMap, 64)
}

fun partTwoOfDay21(): Long {
    val gardenMap = File("src/main/resources/day21.txt")
        .readLines()
        .toMutableList()

    val startPoint = gardenMap.joinToString("").indexOf('S')
    gardenMap[startPoint / gardenMap.size] = gardenMap[startPoint / gardenMap.size].replace('S', '.')
    val repeatedGardenMap = gardenMap.repeatMatrix(4, 4).toMutableList()
    repeatedGardenMap[2 * gardenMap.size + startPoint / gardenMap.size] =
        repeatedGardenMap[2 * gardenMap.size + startPoint / gardenMap.size].replaceRange(2 * gardenMap.size + startPoint % gardenMap.size..2 * gardenMap.size + startPoint % gardenMap.size, "S")

    // Quadratic Equation function
    // 26501365 = 202300 * 131 + 65 where 131 is the dimension of the grid
    // f(x) = 131 * x + 65
    val y1 = nextStepInGarden(repeatedGardenMap, 65)
    val y2 = nextStepInGarden(repeatedGardenMap, 65 + 131)
    val y3 = nextStepInGarden(repeatedGardenMap, 65 + 2 * 131)

    // a * x^2 + b * x + c = y
    // a * 0 + b * 0 + c = y1
    // a * 1 + b * 1 + c = y2
    // a * 4 + b * 2 + c = y3
    //     [0, 0, 1]         [ 0.5,-1.0, 0.5]
    // A = [1, 1, 1]  A^-1 = [-1.5, 2.0,-0.5]
    //     [4, 2, 1]         [ 1.0, 0.0, 0.0]
    val invertedQuadraticMatrix = listOf(listOf(0.5,-1.0,0.5), listOf(-1.5,2.0,-0.5), listOf(1.0,0.0,0.0))
    val yVector = listOf(y1, y2, y3)

    val b = invertedQuadraticMatrix.multiply(yVector)
    val x = 202300
    return b[0].toLong() * x * x + b[1].toLong() * x + b[2].toLong()
}