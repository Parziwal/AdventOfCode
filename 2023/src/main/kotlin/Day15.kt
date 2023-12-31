import java.io.File

fun main() {
    println("Day 15")
    println("-----------------------------------")
    println("Part One: ${partOneOfDay15()}")
    println("Part Two: ${partTwoOfDay15()}")
    println("-----------------------------------")
}

data class Lens(val label: String, var focus: Int)

fun String.getHashValue(): Int {
    var hash = 0
    this.forEach {
        hash += it.code
        hash = hash * 17 % 256
    }
    return hash
}

fun partOneOfDay15(): Int {
    return File("src/main/resources/day15.txt")
        .readText()
        .split(",")
        .sumOf { step ->
            step.getHashValue()
        }
}

fun partTwoOfDay15(): Int {
    val boxes = mutableMapOf<Int, MutableList<Lens>>()
        .apply {
            for (i in 0..255) {
                put(i, mutableListOf())
            }
        }
    File("src/main/resources/day15.txt")
        .readText()
        .split(",")
        .forEach { step ->
            if (step.contains('-')) {
                val label = step.split('-')[0]
                val hash = label.getHashValue()
                boxes[hash]!!.removeAll { it.label == label }
            } else if (step.contains('=')) {
                val label = step.split('=')[0]
                val hash = label.getHashValue()
                val focus = step.split('=')[1][0].digitToInt()
                val existingLens = boxes[hash]!!.find { it.label == label }

                if (existingLens != null) {
                    existingLens.focus = focus
                } else {
                    boxes[hash]!!.add(Lens(label, focus))
                }
            }
        }

    return boxes.entries.sumOf { box ->
        box.value.mapIndexed { slot, lens ->
            (box.key + 1) * (slot + 1) * lens.focus
        }.sum()
    }
}