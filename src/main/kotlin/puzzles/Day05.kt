package puzzles

import DailyPuzzle
import run
import kotlin.math.sign

suspend fun main() = Day5().run()

class Day5 : DailyPuzzle(5) {
    fun solve(ignoreDiagonals: Boolean): String {
        val size = 1000
        val grid = Array(size) { Array(size) { 0 } }
        val regex = """(\d+),(\d+) -> (\d+),(\d+)""".toRegex()
        for (line in readGroup()) {
            val (x1, y1, x2, y2) = regex.matchEntire(line)!!.groupValues.drop(1).map { it.toInt() }
            val sx = (x2 - x1).sign
            val sy = (y2 - y1).sign
            if (ignoreDiagonals && sx*sy != 0) continue
            var x = x1
            var y = y1
            grid[y][x]++

            while (x != x2 || y != y2) {
                x += sx
                y += sy
                grid[y][x]++
            }
        }
        return grid.flatten().count { it > 1 }.toString()
    }

    override fun solvePart1() = solve(ignoreDiagonals = true)
    override fun solvePart2() = solve(ignoreDiagonals = false)

    override val sampleInput = """0,9 -> 5,9
8,0 -> 0,8
9,4 -> 3,4
2,2 -> 2,1
7,0 -> 7,4
6,4 -> 2,0
0,9 -> 2,9
3,4 -> 1,4
0,0 -> 8,8
5,5 -> 8,2""".trimIndent()
    override val sampleAnswerPart1 = "5"
    override val sampleAnswerPart2 = "12"
}