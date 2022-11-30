package puzzles

import DailyPuzzle
import run

suspend fun main() = Day03().run()

class Day03: DailyPuzzle(3) {
    override fun solvePart1(): String {
        val grid = readGroup()
        val gammaDigits = List(grid[0].length) { j ->
            val groups = grid.map { it[j] }.groupBy { it }.mapValues { it.value.size }
            if (groups.getOrDefault('0', 0) > groups.getOrDefault('1', 0)) '1' else '0'
        }
        val gamma = gammaDigits.joinToString("").toInt(2)
        val epsilon = (1 shl grid[0].length) - 1 - gamma
        return (gamma * epsilon).toString()
    }

    override fun solvePart2(): String {
        val lines = readGroup()
        fun procedure(crit: (List<String>, Int) -> (String) -> Boolean): Int {
            var subset = lines.toList()
            repeat(lines[0].length) { j ->
                subset = subset.filter(crit(subset, j))
                if (subset.size == 1) return subset[0].toInt(2)
            }
            throw IllegalStateException("Still too many lines :(")
        }

        val oxygen = procedure { subset, j -> {
            val most = subset.map { it[j] }.groupBy { it }.mapValues { it.value.size }
                .maxByOrNull { it.value * 10 + it.key.digitToInt() }!!.key
            it[j] == most
        } }
        val co2 = procedure { subset, j -> {
            val least = subset.map { it[j] }.groupBy { it }.mapValues { it.value.size }
                .minByOrNull { it.value * 10 + it.key.digitToInt() }!!.key
            it[j] == least
        } }
        return (oxygen * co2).toString()
    }

    override val sampleInput: String = """00100
11110
10110
10111
10101
01111
00111
11100
10000
11001
00010
01010""".trimIndent()

    override val sampleAnswerPart1 = "198"
    override val sampleAnswerPart2 = "230"
}