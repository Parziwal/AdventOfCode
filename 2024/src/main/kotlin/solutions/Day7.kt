package adventofcode.solutions

import core.AoCDay
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

object Day7 : AoCDay<List<Day7.Equation>>(7) {
    override fun transformInput(input: String): List<Equation> {
        return input
            .lineSequence()
            .map { line ->
                val (result, numberList) = line.split(": ")
                val numbers = numberList.split(" ")
                    .map { it.toLong() }
                Equation(result.toLong(), numbers)
            }.toList()
    }

    override fun partOne(): Number {
        return input
            .map { equation ->
                return@map if (equation.isSolvable(setOf(Operator.Add, Operator.Multiply)))
                    equation.result
                else
                    0
            }.sum()
    }

    override fun partTwo(): Number {
        return input
            .map { equation ->
                return@map if (equation.isSolvable(setOf(Operator.Add, Operator.Multiply, Operator.Concatenation)))
                    equation.result
                else
                    0
            }.sum()
    }

    data class Equation(val result: Long, val operands: List<Long>) {
        fun isSolvable(operators: Set<Operator>): Boolean {
            fun findSolution(acc: Long, index: Int): Boolean = when {
                acc > result -> false
                index + 1 >= operands.size -> acc == result
                else -> operators.any { findSolution(it.calculate(acc, operands[index + 1]), index + 1) }
            }

            return findSolution(operands[0], 0)
        }
    }

    enum class Operator {
        Add {
            override fun calculate(num1: Long, num2: Long): Long {
                return num1 + num2
            }
        },
        Multiply {
            override fun calculate(num1: Long, num2: Long): Long {
                return num1 * num2
            }
        },
        Concatenation {
            override fun calculate(num1: Long, num2: Long): Long {
                return (10.0.pow(floor(log10(num2.toDouble())) + 1) * num1).toLong() + num2
            }
        };

        abstract fun calculate(num1: Long, num2: Long): Long
    }
}