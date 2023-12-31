import java.io.File
import kotlin.math.max
import kotlin.math.min

fun main() {
    println("Day 19")
    println("-----------------------------------")
    println("Part One: ${partOneOfDay19()}")
    println("Part Two: ${partTwoOfDay19()}")
    println("-----------------------------------")
}

data class AplentyPart(val x: Int, val m: Int, val  a: Int, val s: Int) {
    fun getCategory(category: String): Int {
        return when(category) {
            "x" -> x
            "m" -> m
            "a" -> a
            "s" -> s
            else -> throw Exception()
        }
    }

    val total = x + m + a + s
}

data class AplentyPartRange(
    val x: IntRange,
    val m: IntRange,
    val a: IntRange,
    val s: IntRange,
    var workflowName: String?) {
    fun getCategory(category: String): IntRange {
        return when(category) {
            "x" -> x
            "m" -> m
            "a" -> a
            "s" -> s
            else -> throw Exception()
        }
    }

    fun copy(category: String, value: IntRange, workflowName: String?): AplentyPartRange {
        return when(category) {
            "x" -> this.copy(x = value, workflowName = workflowName)
            "m" -> this.copy(m = value, workflowName = workflowName)
            "a" -> this.copy(a = value, workflowName = workflowName)
            "s" -> this.copy(s = value, workflowName = workflowName)
            else -> throw Exception()
        }
    }

    val combinations = (x.last - x.first + 1).toLong() * (m.last - m.first + 1).toLong() * (a.last - a.first + 1).toLong() * (s.last - s.first + 1).toLong()
}

data class AplentyRule(private val rule: String) {
    private val result: String
    private val evaluateCondition: (AplentyPart) -> Boolean
    private val splitPartRange: (AplentyPartRange) -> List<AplentyPartRange>

    init {
        if (!rule.contains(':')) {
            result = rule
            evaluateCondition = { _ -> true }
            splitPartRange = { partRange ->
                listOf(partRange.copy(workflowName = result))
            }
        } else {
            result = rule.split(':')[1]
            val condition = rule.split(':')[0]
            val partCategory = condition.split('>', '<')[0]
            val conditionValue = condition.split('>', '<')[1].toInt()
            evaluateCondition = { part ->
                val partValue = part.getCategory(partCategory)
                if (condition.contains('>')) {
                    partValue > conditionValue
                } else {
                    partValue < conditionValue
                }
            }

            splitPartRange = { partRange ->
                val partRanges = mutableListOf<AplentyPartRange>()
                val partValue = partRange.getCategory(partCategory)

                val newPartRange1 = partRange.copy(partCategory,
                    max(partValue.first, conditionValue + if(condition.contains('>')) 1 else 0)..partValue.last,
                    if(condition.contains('>')) result else null
                )
                val newPartRange2 = partRange.copy(partCategory,
                    partValue.first..min(partValue.last, conditionValue - if(condition.contains('<')) 1 else 0),
                    if(condition.contains('<')) result else null
                )

                val newPartRangeValue1 = newPartRange1.getCategory(partCategory)
                val newPartRangeValue2 = newPartRange2.getCategory(partCategory)
                if (newPartRangeValue1.first <= newPartRangeValue1.last) {
                    partRanges.add(newPartRange1)
                }
                if (newPartRangeValue2.first <= newPartRangeValue2.last) {
                    partRanges.add(newPartRange2)
                }

                partRanges
            }
        }
    }

    fun evaluate(part: AplentyPart): String? {
        return if (evaluateCondition(part)) result else null
    }

    fun getPartRanges(partRange: AplentyPartRange): List<AplentyPartRange> {
        return splitPartRange(partRange)
    }
}

data class AplentyWorkflow(val name: String, val rules: List<AplentyRule>) {
    fun evaluate(part: AplentyPart): String {
        for (rule in rules) {
            val result = rule.evaluate(part)
            if (result != null) {
                return result
            }
        }

        throw Exception()
    }

    fun getPartRanges(partRange: AplentyPartRange): List<AplentyPartRange> {
        val partRanges = mutableListOf<AplentyPartRange>()
        var previousRuleResult = partRange

        for (rule in rules) {
            val result = rule.getPartRanges(previousRuleResult)
            partRanges.addAll(result.filter { it.workflowName != null })

            if (result.any { it.workflowName == null }) {
                previousRuleResult = result.first { it.workflowName == null }
            }
        }

        return partRanges
    }
}

fun partOneOfDay19(): Int {
    val workflows = mutableMapOf<String, AplentyWorkflow>()
    val parts = mutableListOf<AplentyPart>()
    var readWorkflows = true

    File("src/main/resources/day19.txt")
        .forEachLine { line ->
            if (line.isEmpty()) {
                readWorkflows = false
                return@forEachLine
            }

            if (readWorkflows) {
                val workflowName = line.split('{')[0]
                val rules = line.split('{')[1].trim('}')
                    .split(',')
                    .map { rule -> AplentyRule(rule) }

                workflows[workflowName] = AplentyWorkflow(workflowName, rules)
            } else {
                val categoryValues = line.trim('{', '}')
                    .split(',')
                    .map { it.trim('x', 'm', 'a', 's', '=').toInt() }
                parts.add(AplentyPart(categoryValues[0], categoryValues[1], categoryValues[2], categoryValues[3]))
            }
        }

    return parts.filter { part ->
        var result = "in"
        while (result != "A" && result != "R") {
            result = workflows[result]!!.evaluate(part)
        }
        result == "A"
    }.sumOf {
        it.total
    }
}

fun partTwoOfDay19(): Long {
    val workflows = mutableMapOf<String, AplentyWorkflow>()
    var readWorkflows = true

    File("src/main/resources/day19.txt")
        .forEachLine { line ->
            if (line.isEmpty()) {
                readWorkflows = false
                return@forEachLine
            }

            if (readWorkflows) {
                val workflowName = line.split('{')[0]
                val rules = line.split('{')[1].trim('}')
                    .split(',')
                    .map { rule -> AplentyRule(rule) }

                workflows[workflowName] = AplentyWorkflow(workflowName, rules)
            }
        }
    
    val startPartRange = AplentyPartRange(1..4000, 1..4000, 1..4000, 1..4000, "in")
    val allPartRanges = mutableListOf(startPartRange)

    while (allPartRanges.any { it.workflowName != "A" && it.workflowName != "R" }) {
        allPartRanges.filter { it.workflowName != "A" && it.workflowName != "R" }
            .forEach {
                val result = workflows[it.workflowName]!!.getPartRanges(it)
                allPartRanges.remove(it)
                allPartRanges.addAll(result)
            }
    }

    return allPartRanges.filter { it.workflowName == "A" }.sumOf {
        it.combinations
    }
}