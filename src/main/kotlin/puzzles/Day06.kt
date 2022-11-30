package puzzles

import DailyPuzzle
import run

suspend fun main() = Day6().run()

class Day6: DailyPuzzle(6) {
    private fun solve(times: Int): String {
        val ages = scanner.nextLine().split(",").map { it.toInt() }
        val ageGroups = List(9) { i -> ages.count { it == i }.toLong() }.toMutableList()
        repeat(times) {
            val zeroes = ageGroups.removeAt(0)
            ageGroups[6] += zeroes
            ageGroups += zeroes
        }
        return ageGroups.sum().toString()
    }

    override fun solvePart1() = solve(80)
    override fun solvePart2() = solve(256)

    override val sampleInput = "3,4,3,1,2"
    override val sampleAnswerPart1 = "5934"
    override val sampleAnswerPart2 = "26984457539"
}