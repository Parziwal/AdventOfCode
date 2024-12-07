package adventofcode.solutions

import core.AoCDay

object Day3 : AoCDay<String>(3) {
    override fun transformInput(input: String): String {
        return input
    }

    override fun partOne(): Number {
        val mulRegex = Regex("mul\\(\\d{1,3},\\d{1,3}\\)")

        return mulRegex.findAll(input)
            .map { match ->
                 val numbers = match.value.split(",")
                    .map {
                        it
                            .removePrefix("mul(")
                            .removeSuffix(")")
                            .toInt()
                    }
                return@map numbers[0] * numbers[1]
            }
            .sum()
    }

    override fun partTwo(): Number {
        val mulRegex = Regex("mul\\(\\d{1,3},\\d{1,3}\\)|do\\(\\)|don't\\(\\)")

        var ignoreNumbers = false
        return mulRegex.findAll(input)
            .map { match ->
                when {
                    match.value == "do()" -> {
                        ignoreNumbers = false
                    }
                    match.value == "don't()" -> {
                        ignoreNumbers = true
                    }
                    !ignoreNumbers -> {
                        val numbers = match.value.split(",")
                            .map {
                                it
                                    .removePrefix("mul(")
                                    .removeSuffix(")")
                                    .toInt()
                            }
                        return@map numbers[0] * numbers[1]
                    }
                }

                return@map 0
            }
            .sum()
    }
}