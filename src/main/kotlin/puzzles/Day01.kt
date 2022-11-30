package puzzles

import DailyPuzzle
import run

suspend fun main() = Day01().run()

class Day01: DailyPuzzle(1) {
    override fun solvePart1(): String {
        return "answer"
    }

    override val sampleInput = """
        123
    """.trimIndent()
    override val sampleAnswerPart1 = "456"
    override val sampleAnswerPart2 = null
}