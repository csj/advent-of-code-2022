package puzzles

import DailyPuzzle
import run

suspend fun main() = Day01().run()

class Day01 : DailyPuzzle(1) {
    override fun solvePart1(): String {
        val lines = readGroup().map { it.toInt() }
        return lines.windowed(2).count { it[1] > it[0] }.toString()
    }

    override fun solvePart2(): String {
        val lines = readGroup().map { it.toInt() }
        return lines.windowed(4).count { it[3] > it[0] }.toString()
    }

    override val sampleInput = """
        199
        200
        208
        210
        200
        207
        240
        269
        260
        263
    """.trimIndent()
    override val sampleAnswerPart1 = "7"
    override val sampleAnswerPart2 = "5"
}

