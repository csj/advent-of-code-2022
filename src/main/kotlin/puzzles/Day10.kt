package puzzles

import DailyPuzzle
import run

suspend fun main() = Day10().run()

class Day10: DailyPuzzle(10) {
    private fun solve(line: String): Long {
        val stack = mutableListOf<Char>()
        for (c in line) {
            when (c) {
                '(', '[', '{', '<' -> stack.add(c)
                ')' -> stack.removeLast().let { if (it != '(') return -3 }
                ']' -> stack.removeLast().let { if (it != '[') return -57 }
                '}' -> stack.removeLast().let { if (it != '{') return -1197 }
                '>' -> stack.removeLast().let { if (it != '<') return -25137 }
            }
        }
        return stack.reversed().fold(0L) { acc, c -> acc * 5 + when(c) {
            '(' -> 1; '[' -> 2; '{' -> 3; '<' -> 4
            else -> throw IllegalArgumentException("Invalid character: $c")
        } }
    }
    private fun Long.negate() = -this
    override fun solvePart1() = readGroup().map { solve(it) }.filter { it < 0 }.sum().negate().toString()

    private fun List<Long>.median() = sorted().let { it[it.size / 2] }
    override fun solvePart2() = readGroup().map { solve(it) }.filter { it > 0 }.median().toString()

    override val sampleInput = """[({(<(())[]>[[{[]{<()<>>
[(()[<>])]({[<{<<[]>>(
{([(<{}[<>[]}>{[]{[(<()>
(((({<>}<{<{<>}{[]{[]{}
[[<[([]))<([[{}[[()]]]
[{[{({}]{}}([{[{{{}}([]
{<[[]]>}<{[{[{[]{()[[[]
[<(<(<(<{}))><([]([]()
<{([([[(<>()){}]>(<<{{
<{([{{}}[<[[[<>{}]]]>[]]"""
    override val sampleAnswerPart1 = "26397"
    override val sampleAnswerPart2 = "288957"
}