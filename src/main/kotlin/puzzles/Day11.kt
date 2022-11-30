package puzzles

import DailyPuzzle
import run

suspend fun main() = Day11().run()

class Day11 : DailyPuzzle(11) {
    fun updateGrid(grid: List<IntArray>): Int {
        for (i in grid.indices) for (j in grid[i].indices) grid[i][j]++
        val flashPoints = mutableSetOf<Pair<Int, Int>>()
        var flashes = 0
        do {
            var found = false
            for (i in grid.indices) for (j in grid[i].indices) {
                if (i to j in flashPoints) continue
                if (grid[i][j] > 9) {
                    found = true
                    flashPoints.add(Pair(i, j))
                    flashes++
                    for (k in -1..1) for (l in -1..1) {
                        if (i + k < 0 || i + k >= grid.size || j + l < 0 || j + l >= grid[i].size) continue
                        grid[i + k][j + l]++
                    }
                }
            }
        } while (found)
        for ((i, j) in flashPoints) grid[i][j] = 0
        return flashes
    }

    override fun solvePart1(): String {
        val grid = readGroup().map { it.map { it.digitToInt() }.toIntArray() }
        return (0 until 100).sumOf { updateGrid(grid) }.toString()
    }

    override fun solvePart2(): String {
        val grid = readGroup().map { it.map { it.digitToInt() }.toIntArray() }
        return generateSequence(1) { it+1 }.first {
            updateGrid(grid) == grid.size * grid.first().size
        }.toString()
    }

    override val sampleInput = """5483143223
2745854711
5264556173
6141336146
6357385478
4167524645
2176841721
6882881134
4846848554
5283751526"""
    override val sampleAnswerPart1 = "1656"
    override val sampleAnswerPart2 = "195"
}