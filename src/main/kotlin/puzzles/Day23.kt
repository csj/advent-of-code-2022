package puzzles

import DailyPuzzle
import run
import kotlin.math.abs

suspend fun main() = Day23().run()

class Day23: DailyPuzzle(23) {

    class Game(private val roomDepth: Int) {
        private val hall = Array(7) { 0 }
        val rooms = List(4) { mutableListOf<Int>() }

        private val distancesToHalls = listOf(
            listOf(),
            listOf(-2,-1,1,3,5,7,8),
            listOf(-4,-3,-1,1,3,5,6),
            listOf(-6,-5,-3,-1,1,3,4),
            listOf(-8,-7,-5,-3,-1,1,2)
        )

        private fun calcHash(): Long {
            var hash = 0L
            for (j in hall) {
                hash = hash * 5 + j
            }
            for (i in 0..3) {
                for (k in 0..3) {
                    hash *= 5
                    if (k in rooms[i].indices) hash += rooms[i][k]
                }
            }
            return hash
        }

        private val visited = mutableSetOf<Long>()
        private val solved = mutableMapOf<Long, Int?>()

        private fun isImpossible(): Boolean {
            // it will be impossible if there are two things in the hall that obstruct each other from getting home
            for (j1 in 0..5) {
                val i1 = hall[j1]; if (i1 == 0) continue
                for (j2 in j1+1 .. 6) {
                    val i2 = hall[j2]; if (i2 == 0) continue
                    if (distancesToHalls[i1][j1] < 0 &&
                        distancesToHalls[i2][j1] > 0 &&
                        distancesToHalls[i1][j2] > 0 &&
                        distancesToHalls[i2][j2] < 0) return true
                }
            }
            return false
        }

        private fun moves() = sequence {
            fun isClear(i: Int, j: Int): Boolean {
                if (j <= i && ((j+1)..i).any { hall[it] != 0 }) return false
                if (j > i && ((i+1) until j).any { hall[it] != 0 }) return false
                return true
            }

            // first, check for auto-moves. the moment we find one, we can stop
            for (i in 1..4) {
                // move somebody into a room
                if (rooms[i-1].any { it != i }) continue  // would bury something bad

                for (j in 0..6) {
                    if (hall[j] != i) continue
                    if (!isClear(i,j)) continue
                    yield(Triple(j, -i, abs(distancesToHalls[i][j]) + roomDepth - rooms[i-1].size))
                    return@sequence
                }
            }

            // done with auto-moves, now try moving things out instead
            for (i in 1..4) {
                // move somebody out of a room
                if (rooms[i-1].all { it == i }) continue  // no reason to move out

                for (j in 0..6) {
                    if (hall[j] != 0) continue
                    if (!isClear(i,j)) continue
                    yield(Triple(-i, j, abs(distancesToHalls[i][j]) + roomDepth + 1 - rooms[i-1].size))
                }
            }
        }

        private fun printBoard(depth: Int = 0) {
            fun toChar(i: Int) = when (i) { 0 -> "."; 1 -> "A"; 2 -> "B"; 3 -> "C"; 4 -> "D"; else -> "?" }
            fun roomToString(room: List<Int>) = "[" + room.joinToString("") { toChar(it) } + "]"

            println(" ".repeat(depth) + listOf(
                toChar(hall[0]),
                toChar(hall[1]),
                roomToString(rooms[0]),
                toChar(hall[2]),
                roomToString(rooms[1]),
                toChar(hall[3]),
                roomToString(rooms[2]),
                toChar(hall[4]),
                roomToString(rooms[3]),
                toChar(hall[5]),
                toChar(hall[6])
            ).joinToString(" "))
        }

        fun search(depth: Int = 0): Int? {
            if (rooms.withIndex().all { (i, room) -> room.all { it == i+1 } && room.size == roomDepth }) return 0

            val hash = calcHash()
            printBoard(depth)
            solved[hash]?.let { return it }
            if (hash in visited) return null
            visited += hash
            if (isImpossible()) return null

            var cheapest: Int? = null

            val moves = moves().toList()

            for ((a,b,cost) in moves) {
                val piece = if (a < 0) rooms[-a-1].removeAt(rooms[-a-1].size-1) else hall[a].also { hall[a] = 0 }
                val actualCost = when(piece) {
                    1 -> 1; 2 -> 10; 3 -> 100; 4 -> 1000; else -> 0
                } * cost
                if (b < 0) rooms[-b-1].add(piece) else hall[b] = piece

                val searchResult = search(depth+1)
                if (b < 0) rooms[-b-1].removeAt(rooms[-b-1].size-1) else hall[b] = 0
                if (a < 0) rooms[-a-1].add(piece) else hall[a] = piece

                val result = (searchResult ?: continue) + actualCost
                if (cheapest == null || result < cheapest) cheapest = result
            }
            solved[hash] = cheapest
            return cheapest
        }
    }

    private fun solve(insertOtherLines: Boolean, roomDepth: Int): String {
        val lines = readGroup().drop(2).take(2)
        val realLines = listOf(lines[0]) +
                (if (insertOtherLines) listOf("  #D#C#B#A#", "  #D#B#A#C#") else listOf()) +
                listOf(lines[1])

        val game = Game(roomDepth)
        for (line in realLines.reversed()) {
            for (i in 0..3) {
                game.rooms[i].add(line[3 + i*2] - 'A' + 1)
            }
        }
        return game.search().toString()
    }

    override fun solvePart1() = solve(false, 2)
    override fun solvePart2() = solve(true, 4)

    override val samples: List<Sample> = listOf(
        Sample("#############\n" +
                "#...........#\n" +
                "###B#C#B#D###\n" +
                "  #A#D#C#A#\n" +
                "  #########", "12521", "44169")
    )

}