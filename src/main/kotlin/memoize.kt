typealias F<A,B> = (A) -> B
interface Handler<A,B> : (Handler<A,B>) -> F<A,B>

fun <A,B>memoize(f2f: (F<A,B>) -> F<A,B>): F<A,B> {
    val cache = mutableMapOf<A,B>()
    val h = object : Handler<A,B> {
        override fun invoke(h: Handler<A,B>): F<A,B> = f2f { n -> cache.getOrPut(n) { h(h)(n) } }
    }
    return { t -> cache.getOrPut(t) { h(h)(t) } }
}

val memoizedFib = memoize<Int, Int> { f -> { n ->
    println("Computing fib($n)")
    when (n) {
        0 -> 0
        1 -> 1
        else -> f(n - 1) + f(n - 2)
    }
}}

fun main() {
    println(memoizedFib(10))
    println(memoizedFib(10))
}