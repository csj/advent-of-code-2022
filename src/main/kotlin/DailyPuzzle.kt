import java.util.*

abstract class DailyPuzzle(val day: Int) {
    abstract fun solvePart1(): String
    open fun solvePart2(): String { return "" }

    open val sampleInput: String = ""
    open val sampleAnswerPart1: String? = null
    open val sampleAnswerPart2: String? = null

    data class Sample(val input: String, val answerPart1: String? = null, val answerPart2: String? = null)
    open val samples by lazy { listOf(Sample(sampleInput, sampleAnswerPart1, sampleAnswerPart2)) }

    var scanner: Scanner = Scanner(System.`in`)

    fun readGroup(): List<String> = sequence {
        while (scanner.hasNextLine()) {
            val line = scanner.nextLine()!!
            if (line.isBlank()) break
            yield (line)
        }
    }.toList()

    fun readGroups(): List<List<String>> = sequence {
        while (scanner.hasNextLine()) {
            val group = readGroup()
            if (group.isEmpty()) break
            yield (group)
        }
    }.toList()

    fun readToks() = readGroup().flatMap { it.split(" ") }
}