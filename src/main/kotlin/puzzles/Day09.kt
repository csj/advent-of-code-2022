package puzzles

import DailyPuzzle
import run

suspend fun main() = Day9().run()

class Day9 : DailyPuzzle(9) {
    override fun solvePart1(): String {
        val lines = readGroup()
        return (lines.indices).sumOf { i ->
            (0 until lines[0].length).sumOf { j ->
                if (i > 0 && lines[i-1][j] <= lines[i][j]) return@sumOf 0
                if (i < lines.size - 1 && lines[i+1][j] <= lines[i][j]) return@sumOf 0
                if (j > 0 && lines[i][j-1] <= lines[i][j]) return@sumOf 0
                if (j < lines[0].length - 1 && lines[i][j+1] <= lines[i][j]) return@sumOf 0
                (lines[i][j] - '0') + 1
            }
        }.toString()
    }

    override fun solvePart2(): String {
        val lines = readGroup()
        val visited = mutableSetOf<Pair<Int, Int>>()
        fun floodFill(i: Int, j: Int): Int {
            if (i to j in visited) return 0
            if (i < 0 || i >= lines.size || j < 0 || j >= lines[0].length) return 0
            if (lines[i][j] == '9') return 0
            visited.add(i to j)
            return 1 + floodFill(i - 1, j) + floodFill(i + 1, j) + floodFill(i, j - 1) + floodFill(i, j + 1)
        }

        val basins = mutableListOf<Int>()
        for (i in lines.indices) {
            for (j in 0 until lines[0].length) {
                if (lines[i][j] != '9' && i to j !in visited) {
                    basins += floodFill(i, j)
                }
            }
        }
        return basins.sortedDescending().take(3).fold(1) { acc, i -> acc * i }.toString()
    }

    override val sampleInput = """2199943210
3987894921
9856789892
8767896789
9899965678"""
    override val sampleAnswerPart1 = "15"
    override val sampleAnswerPart2 = "1134"
}