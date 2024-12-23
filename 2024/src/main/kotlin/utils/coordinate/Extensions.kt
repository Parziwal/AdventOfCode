package adventofcode.utils.coordinate

operator fun <T> List<List<T>>.get(coordinate: Coordinate2D): T {
    return this[coordinate.y][coordinate.x]
}

operator fun <T> List<MutableList<T>>.set(coordinate: Coordinate2D, value: T) {
    this[coordinate.y][coordinate.x] = value
}

fun <T> List<List<T>>.getOrNull(coordinate: Coordinate2D): T? {
    if (this.isNotEmpty() && coordinate.isInside(Coordinate2D(this[0].size - 1, this.size - 1))) {
        return this[coordinate.y][coordinate.x]
    }

    return null
}

operator fun Coordinate2D.plus(other: Coordinate2D): Coordinate2D {
    return Coordinate2D(this.x + other.x, this.y + other.y, other.direction)
}

operator fun Coordinate2D.plus(dir: Direction): Coordinate2D {
    return when(dir) {
        Direction.Up -> this + Coordinate2D(0, -1, dir)
        Direction.Down -> this + Coordinate2D(0, 1, dir)
        Direction.Left -> this + Coordinate2D(-1, 0, dir)
        Direction.Right -> this + Coordinate2D(1, 0, dir)
        Direction.None -> this
    }
}

operator fun Coordinate2D.plus(value: Int): Coordinate2D {
    return this.copy(x = this.x + value, y = this.y + value)
}

operator fun Coordinate2D.minus(other: Coordinate2D): Coordinate2D {
    return Coordinate2D(this.x - other.x, this.y - other.y, other.direction)
}

operator fun Coordinate2D.minus(dir: Direction): Coordinate2D {
    return when(dir) {
        Direction.Up -> this - Coordinate2D(0, -1, dir)
        Direction.Down -> this - Coordinate2D(0, 1, dir)
        Direction.Left -> this - Coordinate2D(-1, 0, dir)
        Direction.Right -> this - Coordinate2D(1, 0, dir)
        Direction.None -> this
    }
}

operator fun Coordinate2D.minus(value: Int): Coordinate2D {
    return this.copy(x = this.x - value, y = this.y - value)
}

operator fun Coordinate2D.rem(modulo: Coordinate2D): Coordinate2D {
    return Coordinate2D(this.x % modulo.x, this.y % modulo.y, modulo.direction)
}

operator fun Coordinate2D.div(divisor: Int): Coordinate2D {
    return this.copy(x = this.x / divisor, y = this.y / divisor)
}

fun Coordinate2D.isInside(size: Coordinate2D, offset: Coordinate2D = Coordinate2D(0, 0)): Boolean {
    return this.x >= offset.x && this.y >= offset.y && this.x <= offset.x + size.x && this.y <= offset.y + size.y
}