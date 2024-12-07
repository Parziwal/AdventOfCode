package core

import java.io.File

abstract class AoCDay<out TInput>(val dayNumber: Int) {
    protected val input: TInput by lazy {
        val input = File("src/main/resources/inputs/day$dayNumber.txt")
            .readText()
        transformInput(input)
    }

    protected abstract fun transformInput(input: String): TInput

    abstract fun partOne(): Number
    abstract fun partTwo(): Number
}