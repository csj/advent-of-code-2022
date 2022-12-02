package puzzles

import DailyPuzzle
import run

suspend fun main() = Day02().run()

class Day02 : DailyPuzzle(2) {

    override fun solvePart1(): String {
        val scorer: (Pair<String, String>) -> Int = { (oppMove, myMove) ->
            val resultScore = when (Pair(oppMove, myMove)) {
                Pair("A", "Y"), Pair("B", "Z"), Pair("C", "X") -> 6
                Pair("A", "Z"), Pair("B", "X"), Pair("C", "Y") -> 0
                Pair("A", "X"), Pair("B", "Y"), Pair("C", "Z") -> 3
                else -> throw Exception("Invalid moves: $oppMove, $myMove")
            }
            val myScore = when (myMove) {
                "X" -> 1; "Y" -> 2; "Z" -> 3
                else -> throw Exception("Invalid move: $myMove")
            }
            resultScore + myScore
        }
        return solve(scorer)
    }

    override fun solvePart2(): String {
        val scorer: (Pair<String, String>) -> Int = { (oppMove, result) ->
            val resultScore = when (result) {
                "X" -> 0; "Y" -> 3; "Z" -> 6
                else -> throw Exception("Invalid result: $result")
            }
            val myScore = when (Pair(oppMove, result)) {
                Pair("A", "Y"), Pair("B", "X"), Pair("C", "Z") -> 1
                Pair("A", "Z"), Pair("B", "Y"), Pair("C", "X") -> 2
                Pair("A", "X"), Pair("B", "Z"), Pair("C", "Y") -> 3
                else -> throw Exception("Invalid input: $oppMove, $result")
            }
            resultScore + myScore
        }
        return solve(scorer)
    }

    fun solve(resultScorer: (Pair<String, String>) -> Int) =
        readGroup().sumOf { line ->
            val (oppMove, myMove) = line.split(" ")
            resultScorer(oppMove to myMove)
        }.toString()

    override val sampleInput = """
        A Y
        B X
        C Z
    """.trimIndent()
    override val sampleAnswerPart1 = "15"
    override val sampleAnswerPart2 = "12"
}