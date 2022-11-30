package puzzles

import DailyPuzzle
import run

suspend fun main() = Day8().run()

class Day8: DailyPuzzle(8) {
    override fun solvePart1(): String {
        val cases = readToks().chunked(15).map { it.drop(11) }
        return cases.flatten().count { it.length in listOf(2,3,4,7)}.toString()
    }

    override fun solvePart2(): String {
        val cases = readToks().chunked(15)
        val digits = listOf(
            "1110111", "0010010", "1011101", "1011011", "0111010", "1101011", "1101111", "1010010", "1111111", "1111011"
        ).map { it.reversed().toInt(2) }

        return cases.sumOf { case ->
            val inputDigits = case.take(10)
            val outputDigits = case.drop(11)

            val freqs = (0..6).map { p -> inputDigits.count { ('a' + p) in it}}.withIndex()
            val four = inputDigits.single { it.length == 4}

            val sourceChars = listOf(
                freqs.indexOfFirst { (p, f) -> f == 8 && ('a' + p) !in four },
                freqs.indexOfFirst { (p, f) -> f == 6 },
                freqs.indexOfFirst { (p, f) -> f == 8 && ('a' + p) in four },
                freqs.indexOfFirst { (p, f) -> f == 7 && ('a' + p) in four },
                freqs.indexOfFirst { (p, f) -> f == 4 },
                freqs.indexOfFirst { (p, f) -> f == 9 },
                freqs.indexOfFirst { (p, f) -> f == 7 && ('a' + p) !in four },
            )

//            println(freqs.joinToString("  ") { (p, f) -> "${('a' + p)}:$f" })
//            println(sourceChars.joinToString("  ") { it.toString() })
//            println(outputDigits.joinToString("  "))
            val outs = outputDigits.map { it.sumOf { 1 shl sourceChars.indexOf(it - 'a') }
                .let {
                    //println(it.toString(2).padStart(7, '0').reversed())
                    digits.indexOf(it)
                } }
//            println(outs.joinToString(" "))
            return@sumOf outs.fold(0) { acc, i -> acc*10 + i }.toInt()
        }.toString()
    }

    override val sampleInput = """be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb |
fdgacbe cefdb cefbgd gcbe
edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec |
fcgedb cgb dgebacf gc
fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef |
cg cg fdcagb cbg
fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega |
efabcd cedba gadfec cb
aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga |
gecf egdcabf bgf bfgea
fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf |
gebdcfa ecba ca fadegcb
dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf |
cefg dcbef fcge gbcadfe
bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd |
ed bcgafe cdgba cbgef
egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg |
gbdfcae bgc cg cgb
gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc |
fgae cfgab fg bagce"""
    override val sampleAnswerPart1 = "26"
    override val sampleAnswerPart2 = "61229"
}