package adventofcode.solutions

import adventofcode.solutions.Day9.DiskBlock
import core.AoCDay
import kotlin.math.min

object Day9 : AoCDay<List<DiskBlock>>(9) {
    override fun transformInput(input: String): List<DiskBlock> {
        var isFreeSpace = false
        var blockId = 0

        return input.mapNotNull { diskSpaceSizeDigit ->
            val diskSpaceSize = diskSpaceSizeDigit.digitToInt()

            var diskBlock: DiskBlock? = null
            if (diskSpaceSize != 0) {
                diskBlock = if (!isFreeSpace) {
                    DiskBlock(blockId++, diskSpaceSize)
                } else {
                    DiskBlock(-1, diskSpaceSize)
                }
            }

            isFreeSpace = !isFreeSpace

            return@mapNotNull diskBlock
        }.toMutableList()
    }

    override fun partOne(): Number {
        val diskBlocks = input.toMutableList()

        var i = diskBlocks.size - 1
        while (i > 0) {
            if (diskBlocks[i].content != -1) {
                val firstFreeSpaceIndex = diskBlocks
                    .indexOfFirst { it.content == -1 }

                if (firstFreeSpaceIndex == -1 || firstFreeSpaceIndex > i) {
                    break
                }

                val freeSpaceSize = diskBlocks[firstFreeSpaceIndex].size
                val diskBlockSize = diskBlocks[i].size
                diskBlocks[firstFreeSpaceIndex] = diskBlocks[i].copy(size = min(freeSpaceSize, diskBlockSize))
                if (freeSpaceSize < diskBlockSize) {
                    diskBlocks[i] = diskBlocks[i].copy(size = diskBlockSize - freeSpaceSize)
                } else {
                    diskBlocks.removeAt(i)
                    diskBlocks.add(firstFreeSpaceIndex + 1, DiskBlock(-1, freeSpaceSize - diskBlockSize))
                }
            } else {
                i--
            }
        }

        return calculateCheckSum(diskBlocks)
    }

    override fun partTwo(): Number {
        val diskBlocks = input.toMutableList()

        var i = diskBlocks.size - 1
        while (i > 0) {
            if (diskBlocks[i].content != -1) {
                val firstFreeSpaceIndex = diskBlocks
                    .indexOfFirst { it.content == -1 && it.size >= diskBlocks[i].size }

                if (firstFreeSpaceIndex != -1 && firstFreeSpaceIndex < i) {
                    val freeSpaceSize = diskBlocks[firstFreeSpaceIndex].size
                    val diskBlockSize = diskBlocks[i].size
                    diskBlocks[firstFreeSpaceIndex] = diskBlocks[i]
                    diskBlocks[i] = DiskBlock(-1, diskBlockSize)

                    if (freeSpaceSize > diskBlockSize) {
                        diskBlocks.add(firstFreeSpaceIndex + 1, DiskBlock(-1, freeSpaceSize - diskBlockSize))
                        i++
                    }
                }
            }

            i--
        }

        return calculateCheckSum(diskBlocks)
    }

    private fun calculateCheckSum(diskBlocks: List<DiskBlock>): Long {
        var checkSum = 0L
        var blockPosition = 0
        for (diskBlock in diskBlocks) {
            if (diskBlock.content == -1) {
                blockPosition += diskBlock.size
                continue
            }

            repeat(diskBlock.size) {
                checkSum += blockPosition * diskBlock.content
                blockPosition++
            }
        }

        return checkSum
    }

    data class DiskBlock(val content: Int, val size: Int)
}