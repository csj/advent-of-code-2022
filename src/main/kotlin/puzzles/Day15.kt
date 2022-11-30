package puzzles

import DailyPuzzle
import run

suspend fun main() = Day15().run()

class Day15: DailyPuzzle(15) {
    private fun solve(tiles: Int): String {
        val grid = readGroup().map { it.map { it.digitToInt() } }
        val nn = grid.size
        val mm = grid[0].size
        val n = grid.size * tiles
        val m = grid[0].size * tiles

        fun cell(i: Int, j: Int) = (grid[i%nn][j%mm] + (i/nn) + (j/mm) - 1) % 9 + 1

        fun Pair<Int, Int>.neighbors() = sequence {
            val (i,j) = this@neighbors
            if (i > 0) yield(Pair(i-1, j))
            if (j > 0) yield(Pair(i, j-1))
            if (i < n - 1) yield(Pair(i+1, j))
            if (j < m - 1) yield(Pair(i, j+1))
        }

        fun dijkstra(start: Pair<Int, Int>, end: Pair<Int, Int>): Int {
            val visited = mutableSetOf<Pair<Int, Int>>()
            val queue = mutableListOf(start)
            val distances = mutableMapOf(start to 0)
            while (queue.isNotEmpty()) {
                val current = queue.minByOrNull { distances[it]!! }!!
                queue.remove(current)
                if (current == end) return distances[current]!!
                visited.add(current)
                val neighbors = current.neighbors()
                for (neighbor in neighbors) {
                    if (visited.contains(neighbor)) continue
                    val distance = distances[current]!! + cell(neighbor.first,neighbor.second)
                    if (distance < (distances[neighbor] ?: Int.MAX_VALUE)) {
                        distances[neighbor] = distance
                        queue.add(neighbor)
                    }
                }
            }
            return -1
        }

        return dijkstra(Pair(0,0), Pair(n-1, m-1)).toString()
    }

    override fun solvePart1() = solve(1)
    override fun solvePart2() = solve(5)

    override val sampleInput = """
        1163751742
        1381373672
        2136511328
        3694931569
        7463417111
        1319128137
        1359912421
        3125421639
        1293138521
        2311944581
    """.trimIndent()
    override val sampleAnswerPart1 = "40"
    override val sampleAnswerPart2 = "315"
}