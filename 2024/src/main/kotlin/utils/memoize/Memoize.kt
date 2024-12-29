package adventofcode.utils.memoize

fun <P, R> useMemoize(initParam: P, function: ((P) -> R, P) -> R): R {
    val cache = mutableMapOf<P, R>()
    lateinit var memoized: (P) -> R
    memoized = { param ->
        if (param in cache) {
            cache[param]!!
        } else {
            val result = function(memoized, param)
            cache[param] = result
            result
        }
    }
    return memoized(initParam)
}

fun <P1, P2, R> useMemoize(initParam1: P1, initParam2: P2, function: ((P1, P2) -> R, P1, P2) -> R): R {
    val cache = mutableMapOf<Pair<P1, P2>, R>()
    lateinit var memoized: (P1, P2) -> R
    memoized = { param1, param2 ->
        val params = Pair(param1, param2)
        if (params in cache) {
            cache[params]!!
        } else {
            val result = function(memoized, param1, param2)
            cache[params] = result
            result
        }
    }
    return memoized(initParam1, initParam2)
}