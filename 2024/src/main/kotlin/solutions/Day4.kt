package adventofcode.solutions

import core.AoCDay

class Day4 : AoCDay(4) {
    override fun partOne(input: String): Number {
        val matrix = input
            .lineSequence()
            .toList()

        var xmasCount = 0
        for ((i, line) in matrix.withIndex()) {
            for ((j, letter) in line.withIndex()) {
                if (letter == 'X') {
                    if (i - 3 >= 0 && matrix[i - 1][j] == 'M' && matrix[i - 2][j] == 'A' && matrix[i - 3][j] == 'S') {
                        xmasCount++
                    }
                    if (i + 3 < matrix.size && matrix[i + 1][j] == 'M' && matrix[i + 2][j] == 'A' && matrix[i + 3][j] == 'S') {
                        xmasCount++
                    }
                    if (j - 3 >= 0 && matrix[i][j - 1] == 'M' && matrix[i][j - 2] == 'A' && matrix[i][j - 3] == 'S') {
                        xmasCount++
                    }
                    if (j + 3 < line.length && matrix[i][j + 1] == 'M' && matrix[i][j + 2] == 'A' && matrix[i][j + 3] == 'S') {
                        xmasCount++
                    }

                    if (i + 3 < matrix.size && j + 3 < line.length && matrix[i + 1][j + 1] == 'M' && matrix[i + 2][j + 2] == 'A' && matrix[i + 3][j + 3] == 'S') {
                        xmasCount++
                    }
                    if (i - 3 >= 0 && j - 3 >= 0 && matrix[i - 1][j - 1] == 'M' && matrix[i - 2][j - 2] == 'A' && matrix[i - 3][j - 3] == 'S') {
                        xmasCount++
                    }
                    if (i + 3 < matrix.size && j - 3 >= 0 && matrix[i + 1][j - 1] == 'M' && matrix[i + 2][j - 2] == 'A' && matrix[i + 3][j - 3] == 'S') {
                        xmasCount++
                    }
                    if (i - 3 >= 0 && j + 3 < line.length && matrix[i - 1][j + 1] == 'M' && matrix[i - 2][j + 2] == 'A' && matrix[i - 3][j + 3] == 'S') {
                        xmasCount++
                    }
                }
            }
        }
        return xmasCount
    }

    override fun partTwo(input: String): Number {
        val matrix = input
            .lineSequence()
            .toList()

        var xmasCount = 0
        for ((i, line) in matrix.withIndex()) {
            for ((j, letter) in line.withIndex()) {
                if (letter == 'A') {
                    if (i + 1 < matrix.size && j + 1 < line.length && i - 1 >= 0 && j - 1 >= 0
                        && ((matrix[i + 1][j + 1] == 'M' && matrix[i - 1][j - 1] == 'S') || (matrix[i + 1][j + 1] == 'S' && matrix[i - 1][j - 1] == 'M'))
                        && ((matrix[i - 1][j + 1] == 'M' && matrix[i + 1][j - 1] == 'S') || (matrix[i - 1][j + 1] == 'S' && matrix[i + 1][j - 1] == 'M'))) {
                        xmasCount++
                    }
                }
            }
        }
        return xmasCount
    }
}