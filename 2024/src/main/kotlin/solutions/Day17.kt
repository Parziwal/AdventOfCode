package adventofcode.solutions

import core.AoCDay

object Day17 : AoCDay<Day17.ChronospatialComputer>(17) {
    override fun transformInput(input: String): ChronospatialComputer {
        val lines = input.lineSequence()
            .toList()

        return ChronospatialComputer(
            lines[0].substringAfter("Register A: ").toLong(),
            lines[1].substringAfter("Register B: ").toLong(),
            lines[2].substringAfter("Register C: ").toLong(),
            lines[4].substringAfter("Program: ")
                .split(",")
                .map { it.toInt() }
        )
    }

    override fun partOne(): Number {
        println("Day 17 |> Part One: ${input.runProgram().joinToString(",")}")
        return 0
    }

    override fun partTwo(): Number {
        return findProgramOutput(input.program).first()
    }

    private fun findProgramOutput(expectedOutput: List<Int>): List<Long> {
        return if (expectedOutput.size == 1) {
            (0L..7L).filter { runProgramIteration(it) == expectedOutput.first() }
        } else {
            findProgramOutput(expectedOutput.drop(1)).flatMap { acc ->
                (acc * 8..acc * 8 + 7).filter { runProgramIteration(it) == expectedOutput.first() }
            }
        }
    }

    private fun runProgramIteration(registerA: Long): Int {
        var registerB = registerA % 8 xor 3
        val registerC = registerA shr registerB.toInt() // shift right is equivalent to a / (2^b1)
        registerB = registerB xor 5
        registerB = registerB xor registerC
        return (registerB % 8).toInt()
    }

    data class ChronospatialComputer(
        val initRegisterA: Long,
        val initRegisterB: Long,
        val initRegisterC: Long,
        val program: List<Int>) {

        private var instructionPointer = 0
        private var registerA = initRegisterA
        private var registerB = initRegisterB
        private var registerC = initRegisterC
        private val outputValues = mutableListOf<Int>()


        fun runProgram(): List<Int> {
            instructionPointer = 0
            registerA = initRegisterA
            registerB = initRegisterB
            registerC = initRegisterC
            outputValues.clear()

            while (instructionPointer < program.size) {
                val opcode = program[instructionPointer]
                val operand = program[instructionPointer + 1]
                executeInstruction(opcode, operand)
            }

            return outputValues
        }

        private fun executeInstruction(opcode: Int, operand: Int) {
            when (opcode) {
                // adv
                0 -> {
                    registerA = registerA shr toComboOperand(operand).toInt()
                    instructionPointer += 2
                }
                // bxl
                1 -> {
                    registerB = registerB xor operand.toLong()
                    instructionPointer += 2
                }
                // bst
                2 -> {
                    registerB = toComboOperand(operand) % 8
                    instructionPointer += 2
                }
                // jnz
                3 -> {
                    if (registerA != 0L) {
                        instructionPointer = operand
                    } else {
                        instructionPointer += 2
                    }
                }
                // bxc
                4 -> {
                    registerB = registerB xor registerC
                    instructionPointer += 2
                }
                // out
                5 -> {
                    outputValues.add((toComboOperand(operand) % 8).toInt())
                    instructionPointer += 2
                }
                // bdv
                6 -> {
                    registerB = registerA shr toComboOperand(operand).toInt()
                    instructionPointer += 2
                }
                // cdv
                7 -> {
                    registerC = registerA shr toComboOperand(operand).toInt()
                    instructionPointer += 2
                }
            }
        }

        private fun toComboOperand(operand: Int): Long {
            return when (operand) {
                in 0..3 -> operand.toLong()
                4 -> registerA
                5 -> registerB
                6 -> registerC
                else -> throw Exception("Invalid operand")
            }
        }
    }
}