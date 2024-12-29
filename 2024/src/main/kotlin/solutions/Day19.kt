package adventofcode.solutions

import adventofcode.utils.memoize.*
import core.AoCDay

object Day19 : AoCDay<Pair<List<String>, List<String>>>(19) {
    override fun transformInput(input: String): Pair<List<String>, List<String>> {
        val towelPatterns = input.lineSequence()
            .first()
            .split(",")
            .map { it.trim() }
        val towelDesigns = input.lineSequence()
            .drop(2)
            .toList()

        return Pair(towelPatterns, towelDesigns)
    }

    override fun partOne(): Number {
        val (towelPatterns, towelDesigns) = input
        return towelDesigns.count { isDesignPossible(it, towelPatterns) }
    }

    override fun partTwo(): Number {
        val (towelPatterns, towelDesigns) = input
        return towelDesigns.sumOf { countPossibleDesigns(it, towelPatterns) }
    }

    private fun isDesignPossible(design: String, patterns: List<String>): Boolean {
        return if (design.isEmpty()) {
            true
        } else {
            patterns.filter { design.startsWith(it) }
                .any { isDesignPossible(design.substringAfter(it), patterns) }
        }
    }

    private fun countPossibleDesigns(designInit: String, patternsInit: List<String>): Long =
        useMemoize(designInit, patternsInit) { self, design, patterns ->
            if (design.isEmpty()) {
                1
            } else {
                patterns.filter { design.startsWith(it) }
                    .sumOf { self(design.substringAfter(it), patterns) }
            }
        }
}