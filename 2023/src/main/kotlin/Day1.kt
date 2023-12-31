import java.io.File

fun main() {
    println("Day 1")
    println("-----------------------------------")
    println("Part One: ${partOneOfDay1()}")
    println("Part Two: ${partTwoOfDay1()}")
    println("-----------------------------------")
}

val NumbersAsStrings = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
val NumbersAsLetters = listOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')

fun String.findFirstNumber(text: String): Int? {
    var temp = text
    while (temp.isNotEmpty()) {
        NumbersAsStrings.forEachIndexed { index, numberAsString ->
            if (temp.startsWith(numberAsString)) {
                return index + 1
            }
        }

        NumbersAsLetters.forEachIndexed { index, numberAsLetter ->
            if (temp.first() == numberAsLetter) {
                return index
            }
        }
        temp = temp.drop(1)
    }

    return null
}

fun String.findLastNumber(text: String): Int? {
    var temp = text
    while (temp.isNotEmpty()) {
        NumbersAsStrings.forEachIndexed { index, numberAsString ->
            if (temp.endsWith(numberAsString)) {
                return index + 1
            }
        }

        NumbersAsLetters.forEachIndexed { index, numberAsLetter ->
            if (temp.last() == numberAsLetter) {
                return index
            }
        }
        temp = temp.dropLast(1)
    }

    return null
}

fun partOneOfDay1(): Int =
    File("src/main/resources/day1.txt")
        .readLines()
        .sumOf { line ->
            val firstDigit = line.find { letter -> letter.isDigit() }!!.digitToInt()
            val lastDigit = line.findLast { letter -> letter.isDigit() }!!.digitToInt()

            firstDigit * 10 + lastDigit
        }

fun partTwoOfDay1() =
    File("src/main/resources/day1.txt")
        .readLines()
        .sumOf { line ->
            val firstDigit = line.findFirstNumber(line)!!
            val lastDigit = line.findLastNumber(line)!!

            firstDigit * 10 + lastDigit
        }