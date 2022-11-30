package puzzles

import DailyPuzzle
import run

suspend fun main() = Day02().run()

class Day02 : DailyPuzzle(2) {
    override fun solvePart1(): String {
        var depth = 0
        var horz = 0
        for (line in readGroup()) {
            "forward (\\d+)".toRegex().find(line)?.let { horz += it.groupValues[1].toInt() }
            "up (\\d+)".toRegex().find(line)?.let { depth -= it.groupValues[1].toInt() }
            "down (\\d+)".toRegex().find(line)?.let { depth += it.groupValues[1].toInt() }
        }
        return (depth * horz).toString()
    }

    override fun solvePart2(): String {
        var depth = 0
        var aim = 0
        var horz = 0
        for (line in readGroup()) {
            "forward (\\d+)".toRegex().find(line)?.let {
                val x = it.groupValues[1].toInt()
                horz += x
                depth += x * aim
            }
            "up (\\d+)".toRegex().find(line)?.let { aim -= it.groupValues[1].toInt() }
            "down (\\d+)".toRegex().find(line)?.let { aim += it.groupValues[1].toInt() }
        }
        return (depth*horz).toString()
    }

    override val sampleInput: String = """
        forward 5
        down 5
        forward 8
        up 3
        down 8
        forward 2
    """.trimIndent()
    override val sampleAnswerPart1: String
        get() = "150"
    override val sampleAnswerPart2: String
        get() = "900"
}