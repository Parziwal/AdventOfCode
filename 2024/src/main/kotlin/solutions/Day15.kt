package adventofcode.solutions

import adventofcode.utils.coordinate.*
import core.AoCDay

object Day15 : AoCDay<Pair<List<List<Char>>, List<Direction>>>(15) {
    override fun transformInput(input: String): Pair<List<List<Char>>, List<Direction>> {
        val inputIterator = input.lineSequence().iterator()

        val warehouseMap = mutableListOf<List<Char>>()
        var line = inputIterator.next()
        while (line.isNotEmpty()) {
            warehouseMap.add(line.toList())
            line = inputIterator.next()
        }

        val directions = mutableListOf<Direction>()
        while (inputIterator.hasNext()) {
            val dirList = inputIterator.next().map {
                when(it) {
                    '^' -> Direction.Up
                    'v' -> Direction.Down
                    '<' -> Direction.Left
                    '>' -> Direction.Right
                    else -> throw Error("Direction not exists")
                }
            }
            directions.addAll(dirList)
        }

        return Pair(warehouseMap, directions)
    }

    override fun partOne(): Number {
        val (warehouseMap, directions) = input.let { pair ->
            Pair(pair.first.map { it.toMutableList() }, pair.second.toMutableList())
        }
        var robotPosition = getRobotStartPosition(warehouseMap)

        while (directions.isNotEmpty()) {
            val dir = directions.removeFirst()
            val robotNextPosition = robotPosition + dir

            var pos = robotNextPosition
            while (warehouseMap[pos] != '.' && warehouseMap[pos] != '#') {
                pos += dir
            }

            when {
                warehouseMap[pos] == '.' && warehouseMap[pos - dir] == '@' -> {
                    warehouseMap[pos] = '@'
                    warehouseMap[pos - dir] = '.'
                    robotPosition = robotNextPosition
                }
                warehouseMap[pos] == '.' && warehouseMap[pos - dir] == 'O' -> {
                    warehouseMap[pos] = 'O'
                    warehouseMap[robotPosition] = '.'
                    warehouseMap[robotNextPosition] = '@'
                    robotPosition = robotNextPosition
                }
            }
        }

        return warehouseMap.mapIndexed { y, row ->
            row.mapIndexed { x, cell ->
                if (cell == 'O') 100 * y + x else 0
            }.sum()
        }.sum()
    }

    override fun partTwo(): Number {
        val warehouseMap = input.first
            .map { row ->
                row.map { cell ->
                    when (cell) {
                        '#' -> cell.toString().repeat(2)
                        '.' -> cell.toString().repeat(2)
                        'O' -> "[]"
                        '@' -> "@."
                        else -> throw Error("Tile type not exists")
                    }
                }
                .map { it.toList() }
                .flatten()
                .toMutableList()
            }
        val directions = input.second.toMutableList()
        var robotPosition = getRobotStartPosition(warehouseMap)

        while (directions.isNotEmpty()) {
            val dir = directions.removeFirst()
            if (canRobotMove(robotPosition, dir, warehouseMap)) {
                moveRobot(robotPosition, dir, warehouseMap)
                robotPosition += dir
            }
        }

        return warehouseMap.mapIndexed { y, row ->
            row.mapIndexed { x, cell ->
                if (cell == '[') 100 * y + x else 0
            }.sum()
        }.sum()
    }

    private fun canRobotMove(currentPosition: Coordinate2D, direction: Direction, warehouseMap: List<MutableList<Char>>): Boolean {
        val nextPosition = currentPosition + direction
        return when {
            warehouseMap[currentPosition] == '.' -> {
                return true
            }
            warehouseMap[currentPosition] == '#' -> {
                return false
            }
            direction == Direction.Left || direction == Direction.Right || (warehouseMap[currentPosition] !in "[]") -> {
                canRobotMove(nextPosition, direction, warehouseMap)
            }
            warehouseMap[currentPosition] == '[' -> {
                canRobotMove(nextPosition, direction, warehouseMap)
                        && canRobotMove(nextPosition + Direction.Right, direction, warehouseMap)
            }
            warehouseMap[currentPosition] == ']' -> {
                canRobotMove(nextPosition,direction, warehouseMap)
                        && canRobotMove(nextPosition + Direction.Left, direction, warehouseMap)
            }
            else -> false
        }
    }

    private fun moveRobot(currentPosition: Coordinate2D, direction: Direction, warehouseMap: List<MutableList<Char>>) {
        val currentTile = warehouseMap[currentPosition]
        if (currentTile == '.') {
            return
        }
        val nextPosition = currentPosition + direction

        moveRobot(nextPosition, direction, warehouseMap)
        if (currentTile == '[' && direction in listOf(Direction.Up, Direction.Down)) {
            moveRobot(nextPosition + Direction.Right, direction, warehouseMap)
        } else if(currentTile == ']' && direction in listOf(Direction.Up, Direction.Down)) {
            moveRobot(nextPosition + Direction.Left, direction, warehouseMap)
        }

        warehouseMap.swapTiles(currentPosition, nextPosition)
        if (currentTile == '[' && direction in listOf(Direction.Up, Direction.Down)) {
            warehouseMap.swapTiles(currentPosition + Direction.Right, nextPosition + Direction.Right)
        } else if(currentTile == ']' && direction in listOf(Direction.Up, Direction.Down)) {
            warehouseMap.swapTiles(currentPosition + Direction.Left, nextPosition + Direction.Left)
        }
    }

    private fun List<MutableList<Char>>.swapTiles(pos1: Coordinate2D, pos2: Coordinate2D) {
        val temp = this[pos1]
        this[pos1] = this[pos2]
        this[pos2] = temp
    }

    private fun getRobotStartPosition(warehouseMap: List<List<Char>>): Coordinate2D {
        for ((rowNumber, row) in warehouseMap.withIndex()) {
            val columnNumber = row.indexOf('@')
            if (columnNumber != -1) {
                return Coordinate2D(columnNumber, rowNumber)
            }
        }

        throw Error("Robot not found")
    }
}