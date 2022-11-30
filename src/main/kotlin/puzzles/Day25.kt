package puzzles

import DailyPuzzle
import run

suspend fun main() = Day25().run()

class Day25: DailyPuzzle(25) {
    private fun solve(): String {
        var grid = readGroup().map { it.toCharArray() }
        val n = grid.size
        val m = grid[0].size

        fun shuffle(isHorz: Boolean): Boolean {
            var found = false
            val newGrid = grid.map { it.clone() }
            for (i in 0 until n) {
                for (j in 0 until m) {
                    if (isHorz && grid[i][j] == '>' && grid[i][(j + 1)%m] == '.') {
                        newGrid[i][j] = '.'
                        newGrid[i][(j + 1)%m] = '>'
                        found = true
                    } else if (!isHorz && grid[i][j] == 'v' && grid[(i + 1)%n][j] == '.') {
                        newGrid[i][j] = '.'
                        newGrid[(i + 1)%n][j] = 'v'
                        found = true
                    }
                }
            }
            grid = newGrid
            return found
        }

        var count = 0
        var found = true
        while (found) {
            found = shuffle(true)
            found = shuffle(false) || found
//            println(grid.joinToString("\n") { it.joinToString("") })
//            readLine()
            count++
        }
        return count.toString()
    }

    override fun solvePart1() = solve()

    override val sampleInput = """
        v...>>.vv>
        .vv>>.vv..
        >>.>v>...v
        >>v>>.>.v.
        v>v.vv.v..
        >.>>..v...
        .vv..>.>v.
        v.v..>>v.v
        ....v..v.>
    """.trimIndent()
    override val sampleAnswerPart1 = "58"
    override val sampleAnswerPart2 = null
}