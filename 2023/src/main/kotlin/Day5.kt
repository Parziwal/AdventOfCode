import java.io.File
import kotlin.math.max
import kotlin.math.min

fun main() {
    println("Day 5")
    println("-----------------------------------")
    println("Part One: ${partOneOfDay5()}")
    println("Part Two: ${partTwoOfDay5()}")
    println("-----------------------------------")
}

data class AlmanacCategory(var value: Long, var converted: Boolean, var range: Long = 1)

fun partOneOfDay5(): Long {
    val almanac = File("src/main/resources/day5.txt")
        .readLines()

    val seedsToLocations = almanac.first()
        .split(' ')
        .drop(1)
        .map { AlmanacCategory(it.toLong(), false) }
        .toMutableList()

    almanac.drop(2)
        .forEach { line ->
            if (line.contains("map")) {
                for (categoryItem in seedsToLocations) {
                    categoryItem.converted = false
                }
                return@forEach
            }

            val maps = line.split(' ')
                .filter { it.isNotEmpty() }
                .map { it.toLong() }

            if (maps.size == 3) {
                val convertValue = maps[0] - maps[1]
                for (categoryItem in seedsToLocations) {
                    if (categoryItem.value in maps[1]..<maps[1] + maps[2] && !categoryItem.converted) {
                        categoryItem.value += convertValue
                        categoryItem.converted = true
                    }
                }
            }
        }

    return seedsToLocations.minOf { it.value }
}

fun partTwoOfDay5(): Long {
    val almanac = File("src/main/resources/day5.txt")
        .readLines()

    val seeds = almanac.first()
        .split(' ')
        .drop(1)
    val seedsToLocations = mutableListOf<AlmanacCategory>()
    for (i in seeds.indices step 2) {
        val categoryItem = AlmanacCategory(seeds[i].toLong(), false, seeds[i + 1].toLong())
        seedsToLocations.add(categoryItem)
    }

    almanac.drop(2)
        .forEach { line ->
            if (line.contains("map")) {
                for (categoryItem in seedsToLocations) {
                    categoryItem.converted = false
                }
                return@forEach
            }

            val maps = line.split(' ')
                .filter { it.isNotEmpty() }
                .map { it.toLong() }

            if (maps.size == 3) {
                val convertValue = maps[0] - maps[1]
                for (i in 0..<seedsToLocations.size) {
                    if (seedsToLocations[i].value < maps[1] + maps[2]
                        && seedsToLocations[i].value + seedsToLocations[i].range > maps[1]
                        && !seedsToLocations[i].converted) {
                        val tempValue = seedsToLocations[i].value
                        val tempRange = seedsToLocations[i].range
                        seedsToLocations[i].value = max(tempValue, maps[1]) + convertValue
                        seedsToLocations[i].range = min(tempValue + tempRange, maps[1] + maps[2]) - max(tempValue, maps[1])
                        seedsToLocations[i].converted = true

                        if (tempValue < maps[1]) {
                            val newCategoryItem = AlmanacCategory(tempValue, false, maps[1] - tempValue + 1)
                            seedsToLocations.add(newCategoryItem)
                        }
                        if (tempValue + tempRange > maps[1] + maps[2]) {
                            val newCategoryItem = AlmanacCategory(maps[1] + maps[2], false, tempValue + tempRange - (maps[1] + maps[2]) + 1)
                            seedsToLocations.add(newCategoryItem)
                        }
                    }
                }
            }
        }

    return seedsToLocations.minOf { it.value }
}