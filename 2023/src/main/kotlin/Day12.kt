import java.io.File

fun main() {
    println("Day 12")
    println("-----------------------------------")
    println("Part One: ${partOneOfDay12()}")
    println("Part Two: ${partTwoOfDay12()}")
    println("-----------------------------------")
}

fun validSpringPermutationsPartOne(springList: String, damagedSprings: List<Int>): Int {
    if (!springList.contains('?')) {
        val damagedSpringsCombination = springList.split('.')
            .filter { it.isNotEmpty() }.map { it.length }
        return if (damagedSpringsCombination == damagedSprings) {
            1
        } else {
            0
        }
    }

    return validSpringPermutationsPartOne(springList.replaceFirst('?', '.'), damagedSprings) +
    validSpringPermutationsPartOne(springList.replaceFirst('?', '#'), damagedSprings)
}

interface Memo2<A, B, R> {
    fun recurse(a: A, b: B): R
}

abstract class Memoized2<A, B, R> {
    private data class Input<A, B>(
        val a: A,
        val b: B
    )

    private val cache = mutableMapOf<Input<A, B>, R>()
    private val memo = object : Memo2<A, B, R> {
        override fun recurse(a: A, b: B): R =
            cache.getOrPut(Input(a, b)) { function(a, b) }
    }

    protected abstract fun Memo2<A, B, R>.function(a: A, b: B): R

    fun execute(a: A, b: B): R = memo.recurse(a, b)
}

fun <A, B, R> (Memo2<A, B, R>.(A, B) -> R).memoize(): (A, B) -> R {
    val memoized = object : Memoized2<A, B, R>() {
        override fun Memo2<A, B, R>.function(a: A, b: B): R = this@memoize(a, b)
    }
    return { a, b ->
        memoized.execute(a, b)
    }
}

fun Memo2<String, List<Int>, Long>.validSpringPermutationsPartTwo(springList: String, springGroups: List<Int>): Long {
    if (springList.isEmpty()) {
        return if (springGroups.isEmpty()) 1 else 0
    }
    if (springGroups.isEmpty()) {
        return if (!springList.contains('#')) 1 else 0
    }

    if (springList.first() == '.') {
        return recurse(springList.drop(1), springGroups)
    }

    if (springList.first() == '#') {
        val group = springGroups.first()
        return if (springList.length >= group
            && springList.substring(0..<group).none { it == '.' }
            && (springList.length == group || springList[group] != '#')
        ) recurse(springList.drop(group + 1), springGroups.drop(1)) else 0
    }

    return recurse(springList.replaceFirst('?', '.'), springGroups) +
            recurse(springList.replaceFirst('?', '#'), springGroups)
}
val validSpringPermutationsPartTwoMemoized = Memo2<String, List<Int>, Long>::validSpringPermutationsPartTwo.memoize()

fun partOneOfDay12() = File("src/main/resources/day12.txt")
    .readLines().sumOf { line ->
        val springList = line.split(' ')[0]
        val damagedSprings = line.split(' ')[1]
            .split(',')
            .map { it.toInt() }

        validSpringPermutationsPartOne(springList, damagedSprings)
    }

fun partTwoOfDay12() = File("src/main/resources/day12.txt")
    .readLines().sumOf { line ->
        val springList = List(5) {
            line.split(' ')[0]
        }.joinToString("?")
        val damagedSprings = List(5) {
            line.split(' ')[1]
                .split(',')
                .map { it.toInt() }
        }.flatten()
        validSpringPermutationsPartTwoMemoized(springList, damagedSprings)
    }