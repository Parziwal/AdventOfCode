package adventofcode.utils.coordinate

operator fun <T> List<List<T>>.get(coordinate: Coordinate2D): T {
    return this[coordinate.y][coordinate.x]
}

operator fun <T> List<MutableList<T>>.set(coordinate: Coordinate2D, value: T) {
    this[coordinate.y][coordinate.x] = value
}

operator fun Coordinate2D.plus(other: Coordinate2D): Coordinate2D {
    return Coordinate2D(this.x + other.x, this.y + other.y, other.direction)
}

operator fun Coordinate2D.minus(other: Coordinate2D): Coordinate2D {
    return Coordinate2D(this.x - other.x, this.y - other.y, other.direction)
}

fun Coordinate2D.isInside(width: Int, height: Int): Boolean {
    return this.x >= 0 && this.y >= 0 && this.x < width && this.y < height
}