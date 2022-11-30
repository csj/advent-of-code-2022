package puzzles

import DailyPuzzle
import run
import kotlin.math.absoluteValue

suspend fun main() = Day7().run()

class Day7: DailyPuzzle(7) {
    private fun solve(cost: (Int) -> Int): String {
        val positions = scanner.nextLine().split(",").map { it.toInt() }
        val min = positions.minOrNull()!!
        val max = positions.maxOrNull()!!
        return (min..max).minOf { pos ->
            positions.sumOf { cost((it - pos).absoluteValue) }
        }.toString()
    }
    override fun solvePart1() = solve { it }
    override fun solvePart2() = solve { it * (it+1) / 2 }

    override val sampleInput = "16,1,2,0,4,2,7,1,2,14"
    override val sampleAnswerPart1 = "37"
    override val sampleAnswerPart2 = "168"
}