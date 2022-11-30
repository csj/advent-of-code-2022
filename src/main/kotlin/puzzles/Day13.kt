package puzzles

import DailyPuzzle
import run

suspend fun main() = Day13().run()

class Day13: DailyPuzzle(13) {
    private fun solve(stopAfterFirstFold: Boolean): String {
        val grid: Array<Array<Boolean>> = Array(1400) { Array(1400) { false } }
        for (line in readGroup()) {
            val (x,y) = line.split(",").map { it.toInt() }
            grid[y][x] = true
        }
        var xSize = 1400
        var ySize = 1400
        for (fold in readGroup()) {
            """fold along y=(\d+)""".toRegex().find(fold)?.let {
                val foldY = it.groupValues[1].toInt()
                for (x in 0 until xSize) {
                    for (y in 0 until foldY) grid[y][x] = grid[y][x] || grid[2*foldY - y][x]
                    for (y in foldY until ySize) grid[y][x] = false
                }
                ySize = foldY
            }
            """fold along x=(\d+)""".toRegex().find(fold)?.let {
                val foldX = it.groupValues[1].toInt()
                for (y in 0 until ySize) {
                    for (x in 0 until foldX) grid[y][x] = grid[y][x] || grid[y][2*foldX - x]
                    for (x in foldX until xSize) grid[y][x] = false
                }
                xSize = foldX
            }
            if (stopAfterFirstFold) break
        }
        if (!stopAfterFirstFold)
            println(grid.take(ySize).joinToString("\n") { line -> line.take(xSize).joinToString("") { if (it) "##" else "  " } })

        return grid.sumOf { it.count { it } }.toString()
    }

    override fun solvePart1() = solve(stopAfterFirstFold = true)
    override fun solvePart2() = solve(stopAfterFirstFold = false)

    override val sampleInput = """6,10
0,14
9,10
0,3
10,4
4,11
6,0
6,12
4,1
0,13
10,12
3,4
3,0
8,4
1,10
2,14
8,10
9,0

fold along y=7
fold along x=5""".trimIndent()
    override val sampleAnswerPart1 = "17"
    override val sampleAnswerPart2 = "16"
}