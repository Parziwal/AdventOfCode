package adventofcode.utils.coordinate

enum class Direction {
    None {
        override val rotateLeft: Direction get() = None
        override val rotateRight: Direction get() = None
        override val opposite: Direction get() = None
    },
    Up {
        override val rotateLeft get() = Left
        override val rotateRight get() = Right
        override val opposite get() = Down
    },
    Down {
        override val rotateLeft get() = Right
        override val rotateRight get() = Left
        override val opposite get() = Up
    },
    Left {
        override val rotateLeft get() = Down
        override val rotateRight get() = Up
        override val opposite get() = Right
    },
    Right {
        override val rotateLeft get() = Up
        override val rotateRight get() = Down
        override val opposite get() = Left
    };

    abstract val rotateLeft: Direction
    abstract val rotateRight: Direction
    abstract val opposite: Direction
}