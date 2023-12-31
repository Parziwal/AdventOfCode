import java.io.File
import kotlin.math.min

fun main() {
    println("Day 13")
    println("-----------------------------------")
    println("Part One: ${partOneOfDay13()}")
    println("Part Two: ${partTwoOfDay13()}")
    println("-----------------------------------")
}

fun List<String>.reflectionLineNumber(): Int? {
    for (i in 0..<this.size - 1) {
        if (this[i] == this[i + 1]) {
            val range = min(i + 1, this.size - i - 1)
            var reflection = true
            for (j in i + 1 - range..i) {
                if (this[j] != this[j + ((i - j + 1) * 2) - 1]) {
                    reflection = false
                    break
                }
            }

            if (reflection) {
                return i + 1
            }
        }
    }

    return null
}

fun List<String>.smudgedReflectionLineNumber(): Int? {
    for (i in 0..<this.size - 1) {
        if (this[i] == this[i + 1] || this[i].differenceCount(this[i + 1]) == 1) {
            val range = min(i + 1, this.size - i - 1)
            var hasOneSmudge = false
            for (j in i + 1 - range..i) {
                val difference = this[j].differenceCount(this[j + ((i - j + 1) * 2) - 1])
                if (difference == 1) {
                    if (hasOneSmudge) {
                        hasOneSmudge = false
                        break
                    }
                    hasOneSmudge = true
                } else if (difference > 1) {
                    hasOneSmudge = false
                    break
                }
            }

            if (hasOneSmudge) {
                return i + 1
            }
        }
    }

    return null
}

fun String.differenceCount(text: String): Int {
    var differences = 0
    for (i in this.indices) {
        if (this[i] != text[i]) {
            differences++
        }
    }

    return differences
}

fun partOneOfDay13(): Int {
    val patterns = File("src/main/resources/day13.txt")
        .useLines {
            val lines = it.toList()

            val patterns = mutableListOf<MutableList<String>>()
            var pattern = mutableListOf<String>()
            lines.forEach { line ->
                if (line.isNotEmpty()) {
                    pattern.add(line)
                } else {
                    patterns.add(pattern)
                    pattern = mutableListOf()
                }
            }
            patterns.add(pattern)

            patterns
        }

    return patterns.sumOf { pattern ->
        val rowsAboveReflection: Int? = pattern.reflectionLineNumber()

        val columns = mutableListOf<String>()
        for (i in 0..<pattern.first().length) {
            var column = ""
            for (j in 0..<pattern.size) {
                column += pattern[j][i]
            }

            columns.add(column)
        }
        val columnsLeftReflection: Int? = columns.reflectionLineNumber()

        if (rowsAboveReflection != null) {
            rowsAboveReflection * 100
        } else {
            columnsLeftReflection!!
        }
    }
}

fun partTwoOfDay13(): Int {
    val patterns = File("src/main/resources/day13.txt")
        .useLines {
            val lines = it.toList()

            val patterns = mutableListOf<MutableList<String>>()
            var pattern = mutableListOf<String>()
            lines.forEach { line ->
                if (line.isNotEmpty()) {
                    pattern.add(line)
                } else {
                    patterns.add(pattern)
                    pattern = mutableListOf()
                }
            }
            patterns.add(pattern)

            patterns
        }

    return patterns.sumOf { pattern ->
        val rowsAboveReflection: Int? = pattern.smudgedReflectionLineNumber()

        val columns = mutableListOf<String>()
        for (i in 0..<pattern.first().length) {
            var column = ""
            for (j in 0..<pattern.size) {
                column += pattern[j][i]
            }

            columns.add(column)
        }
        val columnsLeftReflection: Int? = columns.smudgedReflectionLineNumber()

        if (rowsAboveReflection != null) {
            rowsAboveReflection * 100
        } else {
            columnsLeftReflection!!
        }
    }
}