package puzzles

import DailyPuzzle
import memoize
import run

suspend fun main() = Day17().run()

class Day17: DailyPuzzle(17) {
    private fun triangle(n:Int) = n * (n + 1) / 2

    override fun solvePart1(): String {
        val input = readGroup().first()
        val (x1, x2, y1, y2) = "target area: x=([\\d-]+)..([\\d-]+), y=([\\d-]+)..([\\d-]+)".toRegex()
            .find(input)!!.groupValues.drop(1).map { it.toInt() }
        return triangle(-y1-1).toString()
    }

    override fun solvePart2(): String {
        val input = readGroup().first()
        val (x1, x2, y1, y2) = "target area: x=([\\d-]+)..([\\d-]+), y=([\\d-]+)..([\\d-]+)".toRegex()
            .find(input)!!.groupValues.drop(1).map { it.toInt() }

        var count = 0
        for (ly in y1 until -y1) {
            for (lx in 0 until x2+1) {
                var x = 0
                var y = 0
                var dx = lx
                var dy = ly
                var hits = false

                while(x <= x2 && y >= y1) {
                    x += dx
                    y += dy
                    if (dx > 0) dx--
                    dy--
                    if (x in x1..x2 && y in y1..y2) {
                        hits = true
                        break
                    }
                }
                if (hits) count++
            }
        }
        return count.toString()
    }

    override val sampleInput = """
        target area: x=20..30, y=-10..-5
    """.trimIndent()
    override val sampleAnswerPart1 = "45"
    override val sampleAnswerPart2 = "112"
}