package adventofcode.utils.coordinate

operator fun <T> List<List<T>>.get(coordinate: Coordinate2D): T {
    return this[coordinate.y][coordinate.x]
}

operator fun <T> List<MutableList<T>>.set(coordinate: Coordinate2D, value: T) {
    this[coordinate.y][coordinate.x] = value
}