package adventofcode.solutions

import core.AoCDay

object Day4 : AoCDay<List<String>>(4) {
    override fun transformInput(input: String): List<String> {
        return input.lineSequence()
            .toList()
    }

    override fun partOne(): Number {
        var xmasCount = 0
        for ((i, line) in input.withIndex()) {
            for ((j, letter) in line.withIndex()) {
                if (letter == 'X') {
                    if (i - 3 >= 0 && input[i - 1][j] == 'M' && input[i - 2][j] == 'A' && input[i - 3][j] == 'S') {
                        xmasCount++
                    }
                    if (i + 3 < input.size && input[i + 1][j] == 'M' && input[i + 2][j] == 'A' && input[i + 3][j] == 'S') {
                        xmasCount++
                    }
                    if (j - 3 >= 0 && input[i][j - 1] == 'M' && input[i][j - 2] == 'A' && input[i][j - 3] == 'S') {
                        xmasCount++
                    }
                    if (j + 3 < line.length && input[i][j + 1] == 'M' && input[i][j + 2] == 'A' && input[i][j + 3] == 'S') {
                        xmasCount++
                    }

                    if (i + 3 < input.size && j + 3 < line.length && input[i + 1][j + 1] == 'M' && input[i + 2][j + 2] == 'A' && input[i + 3][j + 3] == 'S') {
                        xmasCount++
                    }
                    if (i - 3 >= 0 && j - 3 >= 0 && input[i - 1][j - 1] == 'M' && input[i - 2][j - 2] == 'A' && input[i - 3][j - 3] == 'S') {
                        xmasCount++
                    }
                    if (i + 3 < input.size && j - 3 >= 0 && input[i + 1][j - 1] == 'M' && input[i + 2][j - 2] == 'A' && input[i + 3][j - 3] == 'S') {
                        xmasCount++
                    }
                    if (i - 3 >= 0 && j + 3 < line.length && input[i - 1][j + 1] == 'M' && input[i - 2][j + 2] == 'A' && input[i - 3][j + 3] == 'S') {
                        xmasCount++
                    }
                }
            }
        }
        return xmasCount
    }

    override fun partTwo(): Number {
        var xmasCount = 0
        for ((i, line) in input.withIndex()) {
            for ((j, letter) in line.withIndex()) {
                if (letter == 'A') {
                    if (i + 1 < input.size && j + 1 < line.length && i - 1 >= 0 && j - 1 >= 0
                        && ((input[i + 1][j + 1] == 'M' && input[i - 1][j - 1] == 'S') || (input[i + 1][j + 1] == 'S' && input[i - 1][j - 1] == 'M'))
                        && ((input[i - 1][j + 1] == 'M' && input[i + 1][j - 1] == 'S') || (input[i - 1][j + 1] == 'S' && input[i + 1][j - 1] == 'M'))) {
                        xmasCount++
                    }
                }
            }
        }
        return xmasCount
    }
}