package puzzles

import DailyPuzzle
import run

suspend fun main() = Day16().run()

class Day16: DailyPuzzle(16) {
    private fun Char.hexToBits() = toString().toInt(16).toString(2)
        .padStart(4, '0').toCharArray().asIterable()

    abstract class Packet { abstract val version: Int }
    data class ValuePacket(override val version: Int, val value: Long): Packet()
    data class OpPacket(override val version: Int, val type: Int, val subPackets: List<Packet>): Packet()

    private fun parse(): Packet {
        val inputBits = readGroup().first().flatMap { it.hexToBits() }.iterator()
        var pointer = 0
        fun getBits(n: Int) = sequence { repeat(n) { pointer++; yield(inputBits.next()) } }.toList()
        fun getNum(n: Int) = getBits(n).joinToString("").toInt(2)

        fun <T>Sequence<T>.takeWhileInclusive(pred: (T) -> Boolean) = sequence {
            for (item in this@takeWhileInclusive) {
                yield(item)
                if (!pred(item)) break
            }
        }

        fun readPacket(): Packet {
            val version = getNum(3)
            val type = getNum(3)

            return when {
                type == 4 -> {
                    val literal = generateSequence { getBits(5) }.takeWhileInclusive { it.first() == '1' }
                        .fold(0L) { acc, five -> acc*16 + five.drop(1).joinToString("").toInt(2) }
                    ValuePacket(version, literal)
                }
                getBits(1).first() == '0' -> {
                    val length = getNum(15)
                    val pointerEnd = pointer + length
                    val childPackets = generateSequence { readPacket() }.takeWhileInclusive { pointer < pointerEnd }.toList()
                    OpPacket(version, type, childPackets)
                }
                else -> {
                    val numChildren = getNum(11)
                    OpPacket(version, type, List(numChildren) { readPacket() })
                }
            }
        }

        return readPacket()
    }

    override fun solvePart1(): String {
        fun Packet.sumOfVersions(): Long =
            version + if (this is OpPacket) subPackets.sumOf { it.sumOfVersions() } else 0L
        return parse().sumOfVersions().toString()
    }

    override fun solvePart2(): String {
        fun Packet.evaluate(): Long = when (this) {
            is ValuePacket -> value
            is OpPacket -> {
                val subValues = subPackets.map { it.evaluate() }
                when(this.type) {
                    0 -> subValues.sum()
                    1 -> subValues.fold(1L) { acc, value -> acc * value }
                    2 -> subValues.minOrNull()!!
                    3 -> subValues.maxOrNull()!!
                    5 -> if (subValues[0] > subValues[1]) 1 else 0
                    6 -> if (subValues[0] < subValues[1]) 1 else 0
                    7 -> if (subValues[0] == subValues[1]) 1 else 0
                    else -> throw IllegalArgumentException("Unknown op type ${this.type}")
                }
            }
            else -> throw IllegalArgumentException("Unknown packet type ${this::class.simpleName}")
        }
        return parse().evaluate().toString()
    }

    override val samples = listOf(
        Sample("D2FE28", "6"),
        Sample("38006F45291200", "9"),
        Sample("8A004A801A8002F478", "16"),
        Sample("620080001611562C8802118E34", "12"),
        Sample("C0015000016115A2E0802F182340", "23"),
        Sample("A0016C880162017C3686B18A3D4780", "31"),

        Sample("C200B40A82", null, "3"),
        Sample("04005AC33890", null, "54"),
        Sample("880086C3E88112", null, "7"),
        Sample("CE00C43D881120", null, "9"),
        Sample("D8005AC2A8F0", null, "1"),
        Sample("F600BC2D8F", null, "0"),
        Sample("9C005AC2F8F0", null, "0"),
        Sample("9C0141080250320F1802104A08", null, "1"),
    )
}


