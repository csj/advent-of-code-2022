import java.util.*

suspend fun DailyPuzzle.run() {
    val actualInput = getInputForDay(this.day)
    println("-- DAY ${this.day} Part 1 --")
    for (sample in this.samples) {
        if (sample.answerPart1 == null) continue
        this.scanner = Scanner(sample.input)
        val part1SampleSolution = this.solvePart1()
        if (part1SampleSolution == sample.answerPart1) {
            println("Sample is correct: $part1SampleSolution")
        } else {
            println("Sample is incorrect (Expected: ${sample.answerPart1}, Actual: $part1SampleSolution)")
            return
        }
    }

    this.scanner = Scanner(actualInput)
    val part1ActualSolution = this.solvePart1()
    println("Solution: $part1ActualSolution")

    if (this.samples.any { it.answerPart2 != null }) {
        println("\n-- DAY ${this.day} Part 2 --")

        for (sample in this.samples) {
            if (sample.answerPart2 == null) continue
            this.scanner = Scanner(sample.input)
            val part2SampleSolution = this.solvePart2()
            if (part2SampleSolution == sample.answerPart2) {
                println("Sample is correct: $part2SampleSolution")
            } else {
                println("Sample is incorrect (Expected: ${sample.answerPart2}, Actual: $part2SampleSolution)")
                return
            }
        }
        this.scanner = Scanner(actualInput)
        val part2ActualSolution = this.solvePart2()
        println("Solution: $part2ActualSolution")
    }
}