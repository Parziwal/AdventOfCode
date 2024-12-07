package adventofcode.utils.coordinate

data class Coordinate2D(val x: Int, val y: Int, val direction: Direction = Direction.Up) {
    fun nextCoordinates(): Coordinate2D {
        return when(direction) {
            Direction.Up -> Coordinate2D(x, y - 1, direction)
            Direction.Down -> Coordinate2D(x, y + 1, direction)
            Direction.Left -> Coordinate2D(x - 1, y, direction)
            Direction.Right -> Coordinate2D(x + 1, y, direction)
        }
    }
}