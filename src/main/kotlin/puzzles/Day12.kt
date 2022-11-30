package puzzles

import DailyPuzzle
import run

suspend fun main() = Day12().run()

class Day12: DailyPuzzle(12) {
    private fun solve(allowDoubleVisit: Boolean): String {
        val lines = readGroup().map { it.split("-") }
        val connections = mutableMapOf<String, MutableList<String>>()
        for ((s,d) in lines) {
            connections.getOrPut(s) { mutableListOf() }.add(d)
            connections.getOrPut(d) { mutableListOf() }.add(s)
        }

        fun search(current: String, revisitAvailable: Boolean, path: List<String>): Long {
            if (current == "end") return 1
            return connections[current]!!
                .filterNot { it == "start" || (!revisitAvailable && it[0].isLowerCase() && it in path) }
                .sumOf { search(it, revisitAvailable && !(it[0].isLowerCase() && it in path), path + it) }
        }

        return search("start", allowDoubleVisit, listOf()).toString()
    }

    override fun solvePart1() = solve(allowDoubleVisit = false)
    override fun solvePart2() = solve(allowDoubleVisit = true)

    override val sampleInput = """fs-end
he-DX
fs-he
start-DX
pj-DX
end-zg
zg-sl
zg-pj
pj-he
RW-he
fs-DX
pj-RW
zg-RW
start-pj
he-WI
zg-he
pj-fs
start-RW""".trimIndent()
    override val sampleAnswerPart1 = "226"
    override val sampleAnswerPart2 = "3509"
}