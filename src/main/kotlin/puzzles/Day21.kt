package puzzles

import DailyPuzzle
import memoize
import run
import java.lang.Long.max

suspend fun main() = Day21().run()

class Day21: DailyPuzzle(21) {
    private fun readPositions(): IntArray =
        readGroup().map { it.substring("Player x starting position: ".length).toInt() }.toIntArray()

    override fun solvePart1(): String {
        class Dice {
            var totalRolls = 0

            private fun diceSequence() = sequence {
                totalRolls = 0
                while (true) repeat(100) {
                    totalRolls++
                    yield(it+1)
                }
            }
            private val diceIterator = diceSequence().iterator()
            fun roll() = diceIterator.next()
        }

        val scores = Array(2) { 0 }
        val positions = readPositions()
        var current = 0
        val dice = Dice()

        while (true) {
            repeat(3) {
                val roll = dice.roll()
                positions[current] = (positions[current] + roll - 1) % 10 + 1
            }
            scores[current] += positions[current]
            if (scores[current] >= 1000) {
                return (dice.totalRolls * scores[1-current]).toString()
            }
            current = 1 - current
        }
    }

    override fun solvePart2(): String {
        data class GameState(val p1Score: Int, val p2Score: Int, val p1Position: Int, val p2Position: Int, val p1ToMove: Boolean)
        val rollDist = mapOf(
            3 to 1L, // 111
            4 to 3L, // 112 121 211
            5 to 6L, // 122 212 221 113 131 311
            6 to 7L, // 123*6, 222
            7 to 6L,
            8 to 3L,
            9 to 1L
        )

        val solve = memoize<GameState, Pair<Long, Long>> { f -> { state ->
            // from this state, how many games will each of the players win?
            if (state.p1Score >= 21) Pair(1L, 0L)
            else if (state.p2Score >= 21) Pair(0L, 1L)
            else {
                rollDist.map { (roll, freq) ->
                    val (p1Wins, p2Wins) = if (state.p1ToMove) {
                        val newPos = (state.p1Position + roll - 1) % 10 + 1
                        state.copy(p1Score = state.p1Score + newPos, p1Position = newPos, p1ToMove = false).let(f)
                    } else {
                        val newPos = (state.p2Position + roll - 1) % 10 + 1
                        state.copy(p2Score = state.p2Score + newPos, p2Position = newPos, p1ToMove = true).let(f)
                    }
                    Pair(p1Wins * freq, p2Wins * freq)
                }.fold(Pair(0L, 0L)) { (a,b), (c,d) -> Pair(a+c, b+d) }
            }
        } }

        val positions = readPositions()
        val (p1Wins, p2Wins) = GameState(0, 0, positions[0], positions[1], true).let(solve)
        return max(p1Wins, p2Wins).toString()
    }

    override val sampleInput = """
        Player 1 starting position: 4
        Player 2 starting position: 8
    """.trimIndent()
    override val sampleAnswerPart1 = "739785"
    override val sampleAnswerPart2 = "444356092776315"
}