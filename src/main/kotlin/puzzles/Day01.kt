package puzzles

import DailyPuzzle
import run

suspend fun main() = Day01().run()

class Day01 : DailyPuzzle(1) {
    override fun solvePart1() = readGroups().maxOf { it.sumOf { it.toInt() } }.toString()
    override fun solvePart2() = readGroups().map {
        it.sumOf { it.toInt() }
    }.sortedDescending().take(3).sum().toString()

    override val sampleInput = """
        1000
        2000
        3000

        4000

        5000
        6000

        7000
        8000
        9000

        10000
    """.trimIndent()
    override val sampleAnswerPart1 = "24000"
    override val sampleAnswerPart2 = "45000"
}