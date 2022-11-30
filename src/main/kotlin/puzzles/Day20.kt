package puzzles

import DailyPuzzle
import run

suspend fun main() = Day20().run()

class Day20: DailyPuzzle(20) {
    fun solve(iterations: Int): String {
        val algo = readGroup().joinToString("")
        val image = readGroup()
        var grid = image.map { it.map { it == '#' }.toBooleanArray() }

        repeat(iterations) { iter ->
            val newGrid = List(grid.size + 2) { BooleanArray(grid[0].size + 2) { false } }
            for (i in newGrid.indices) {
                for (j in newGrid[0].indices) {
                    var index = 0
                    for (ii in -1..1) for (jj in -1..1) {
                        index *= 2
                        val pixelVal = if (i + ii - 1 in grid.indices && j + jj - 1 in grid[0].indices)
                            grid[i + ii - 1][j + jj - 1]
                        else
                            iter % 2 == 1 && algo[0] == '#'
                        if (pixelVal) index += 1
                    }
                    newGrid[i][j] = algo[index] == '#'
                }
            }

            grid = newGrid
        }

        return grid.sumOf { it.count { it } }.toString()
    }

    override fun solvePart1() = solve(2)
    override fun solvePart2() = solve(50)

    override val sampleInput = """
        ..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..##
        #..######.###...####..#..#####..##..#.#####...##.#.#..#.##..#.#......#.###
        .######.###.####...#.##.##..#..#..#####.....#.#....###..#.##......#.....#.
        .#..#..##..#...##.######.####.####.#.#...#.......#..#.#.#...####.##.#.....
        .#..#...##.#.##..#...##.#.##..###.#......#.#.......#.#.#.####.###.##...#..
        ...####.#..#..#.##.#....##..#.####....##...##..#...#......#.#.......#.....
        ..##..####..#...#.#.#...##..#.#..###..#####........#..####......#..#

        #..#.
        #....
        ##..#
        ..#..
        ..###
    """.trimIndent()
    override val sampleAnswerPart1 = "35"
    override val sampleAnswerPart2 = "3351"
}