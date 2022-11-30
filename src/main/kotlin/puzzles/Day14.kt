package puzzles

import DailyPuzzle
import memoize
import run

suspend fun main() = Day14().run()

class Day14: DailyPuzzle(14) {
    private fun solve(totalSteps: Int): String {
        val start = readGroup().first()
        val transforms = readGroup()
        val letters = transforms.flatMap { it.toList() }.distinct().minus(listOf(' ', '-', '>')).toList().sorted()
        val transformMap = transforms.map { it.split(" -> ") }.associate { it[0] to it[1] }
        fun letterIndex(c: Char) = letters.indexOf(c)
        val n = letters.size

        operator fun List<Long>.plus(other: List<Long>) = zip(other).map { (a,b) -> a + b }

        // calculates for any input string (a pair of characters) and number of steps to simulate,
        // the distribution of stuff after the steps including the first letter, but excluding the last.

        val memoizedSim = memoize<Pair<String, Int>, List<Long>> { f -> { pair ->
            val (input, steps) = pair
            val newLetter = transformMap[input]!!
            if (steps == 0) List(n) { i -> if (i == letterIndex(input[0])) 1 else 0 }
            else f(input[0] + newLetter to steps-1) + f(newLetter + input[1] to steps-1)
        }}

        val endDist = (0 until (start.length - 1)).fold(List(n) { 0L }) { acc, i ->
            acc + memoizedSim(start.substring(i, i+2) to totalSteps)
        } + List(n) { i -> if (i == letterIndex(start[start.length-1])) 1 else 0 }

        return (endDist.maxOrNull()!! - endDist.minOrNull()!!).toString()
    }

    override fun solvePart1() = solve(10)
    override fun solvePart2() = solve(40)

    override val sampleInput = """
        NNCB

        CH -> B
        HH -> N
        CB -> H
        NH -> C
        HB -> C
        HC -> B
        HN -> C
        NN -> C
        BH -> H
        NC -> B
        NB -> B
        BN -> B
        BB -> N
        BC -> B
        CC -> N
        CN -> C
    """.trimIndent()
    override val sampleAnswerPart1 = "1588"
    override val sampleAnswerPart2 = "2188189693529"
}