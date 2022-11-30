package puzzles

import DailyPuzzle
import run

suspend fun main() = Day4().run()

class Day4: DailyPuzzle(4) {
    data class BoardResult(val moves: Int, val score: Int)

    private fun solve(board: List<String>, numbersCalled: List<Int>): BoardResult {
        val marks = List(5) { Array(5) { false } }
        val boardNums = board.map { it.split(" ").filterNot {it.isEmpty()}.map { it.toInt() } }

        for ((index, num) in numbersCalled.withIndex()) {
            val (i,j) = (0..4).flatMap { i -> (0..4).map { j -> Pair(i,j) } }
                .firstOrNull { (i,j) -> boardNums[i][j] == num } ?: continue
            marks[i][j] = true
            if ((0..4).any { k ->
                (0..4).all { i -> marks[i][k] } ||
                (0..4).all { j -> marks[k][j] }
            }) {
                val unmarkedSum = (0..4).flatMap { i -> (0..4).map { j -> Pair(i,j) } }
                    .filter { (i,j) -> !marks[i][j] }
                    .sumOf { (i,j) -> boardNums[i][j] }
                return BoardResult(index+1, num * unmarkedSum)
            }
        }
        throw IllegalStateException("No solution found")
    }

    private fun solveAllBoards(): List<BoardResult> {
        val numbersCalled = scanner.nextLine().split(",").map { it.toInt() }
        scanner.nextLine()
        return readGroups().map { solve(it, numbersCalled) }
    }

    override fun solvePart1(): String {
        val boardResults = solveAllBoards()
        val firstWinner = boardResults.minByOrNull { it.moves }!!
        return firstWinner.score.toString()
    }

    override fun solvePart2(): String {
        val boardResults = solveAllBoards()
        val lastWinner = boardResults.maxByOrNull { it.moves }!!
        return lastWinner.score.toString()
    }

    override val sampleInput = """7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1

22 13 17 11  0
 8  2 23  4 24
21  9 14 16  7
 6 10  3 18  5
 1 12 20 15 19

 3 15  0  2 22
 9 18 13 17  5
19  8  7 25 23
20 11 10 24  4
14 21 16 12  6

14 21 17 24  4
10 16 15  9 19
18  8 23 26 20
22 11 13  6  5
 2  0 12  3  7
    """.trimIndent()
    override val sampleAnswerPart1 = "4512"
    override val sampleAnswerPart2 = "1924"
}