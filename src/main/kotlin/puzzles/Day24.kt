package puzzles

import DailyPuzzle
import run

suspend fun main() = Day24().run()

class Day24: DailyPuzzle(24) {

    abstract class Node {
        open fun simplify() = this
        fun simplifyFully(): Node {
            var current = this
            while (true) {
                val next = current.simplify()
                if (next == current) return current
                current = next
            }
        }
        abstract val range: IntRange
    }

    class ValueNode(val value: Int): Node() {
        override fun toString() = value.toString()
        override val range = value..value
    }

    class VariableNode(val name: Char): Node() {
        override fun toString() = name.toString()
        override val range = 1..9
    }

    class OpNode(private val op: String, private val left: Node, private val right: Node): Node() {
        override fun toString(): String {
            if (left is ValueNode && right is VariableNode && op == "*") return "${left.value}${right.name}"
            return "($left $op $right)"
        }

        override val range: IntRange = when(op) {
            "+" -> (left.range.first + right.range.first)..(left.range.last + right.range.last)
            "*" -> {
                if (right is ValueNode) {
                    if (right.value == 0) 0..0
                    else if (right.value > 0) (left.range.first * right.value)..(left.range.last * right.value)
                    else (left.range.last * right.value)..(left.range.first * right.value)
                } else -1000000..1000000
            }
            "/" -> {
                if (right is ValueNode) {
                    if (right.value > 0) (left.range.first / right.value)..(left.range.last / right.value)
                    else (left.range.last / right.value)..(left.range.first / right.value)
                } else -1000000..1000000
            }
            "%" -> {
                if (right is ValueNode) {
                    0..right.value
                } else -1000000..1000000
            }
            "==" -> 0..1
            "!=" -> 0..1
            else -> throw IllegalArgumentException("Unknown operator $op")
        }

        override fun simplify(): Node {
            when (op) {
                "+" -> {
                    if (left is ValueNode && left.value == 0) return right
                    if (right is ValueNode && right.value == 0) return left
                    if (left is ValueNode && right is ValueNode) return ValueNode(left.value + right.value)
                    if (left is OpNode && right is ValueNode) {
                        if (left.op == "+" && left.right is ValueNode)
                            return OpNode("+", left.left, ValueNode(left.right.value + right.value)).simplifyFully()
                    }
                }
                "*" -> {
                    if (left is ValueNode && left.value == 0) return ValueNode(0)
                    if (right is ValueNode && right.value == 0) return ValueNode(0)
                    if (left is ValueNode && left.value == 1) return right
                    if (right is ValueNode && right.value == 1) return left
                    if (left is ValueNode && right is ValueNode) return ValueNode(left.value * right.value)

                }
                "/" -> {
                    if (left is ValueNode && left.value == 0) return ValueNode(0)
                    if (right is ValueNode && right.value == 1) return left
                    if (right is ValueNode && left.range.last <= right.value) return ValueNode(0)
                    if (left is ValueNode && right is ValueNode) return ValueNode(left.value / right.value)
                    if (left is OpNode && left.op == "+" && right is ValueNode) {
                        if (left.left is OpNode && left.left.op == "*" && left.left.right is ValueNode
                            && left.left.right.value == right.value) {
                            if (left.right.range.last < right.value)
                                return left.left.left
                        }
                    }
                }
                "%" -> {
                    if (left is ValueNode && left.value == 0) return ValueNode(0)
                    if (right is ValueNode && left.range.last <= right.value) return left
                    if (left is ValueNode && right is ValueNode) return ValueNode(left.value % right.value)
                    if (left is OpNode && left.op == "+" && right is ValueNode) {
                        if (left.left is OpNode && left.left.op == "*" && left.left.right is ValueNode &&
                                left.left.right.value == right.value) return left.right
                    }
                }
                "==" -> {
                    if (right is ValueNode && right.value == 0 && left is OpNode && left.op == "==")
                        return OpNode("!=", left.left, left.right)
                    if (left.range.last < right.range.first) return ValueNode(0)
                    if (left.range.first > right.range.last) return ValueNode(0)
                    if (left is ValueNode && right is ValueNode)
                        return if (left.value == right.value) ValueNode(1) else ValueNode(0)
                }
                "!=" -> {
                    if (right is ValueNode && right.value == 0 && left is OpNode && left.op == "==")
                        return OpNode("==", left.left, left.right)
                    if (left.range.last < right.range.first) return ValueNode(1)
                    if (left.range.first > right.range.last) return ValueNode(1)
                    if (left is ValueNode && right is ValueNode)
                        return if (left.value == right.value) ValueNode(0) else ValueNode(1)
                }
            }
            return this
        }
    }

    class World(
        val registers: MutableList<Node> = List(4) { ValueNode(0) }.toMutableList(),
        val conditions: List<String> = emptyList(),
        var nextVariable: Char = 'a'
    ) {
        fun parseTok2(tok2: String): Node {
            return when (tok2) {
                "w" -> registers[0]
                "x" -> registers[1]
                "y" -> registers[2]
                "z" -> registers[3]
                else -> ValueNode(tok2.toInt())
            }
        }

        fun process(toks: List<String>): List<World> {
            val regIndex = toks[1][0] - 'w'
            when (toks[0]) {
                "inp" -> registers[regIndex] = VariableNode(nextVariable++)
                "add" -> registers[regIndex] = OpNode("+", registers[regIndex], parseTok2(toks[2])).simplifyFully()
                "mul" -> registers[regIndex] = OpNode("*", registers[regIndex], parseTok2(toks[2])).simplifyFully()
                "div" -> registers[regIndex] = OpNode("/", registers[regIndex], parseTok2(toks[2])).simplifyFully()
                "mod" -> registers[regIndex] = OpNode("%", registers[regIndex], parseTok2(toks[2])).simplifyFully()
                "eql" -> {
                    val left = registers[regIndex]
                    val right = parseTok2(toks[2])

                    // distinguish between *always true*, *always false*, and *maybe true*
                    if (left.range.last < right.range.first || right.range.last < left.range.first) {
                        // always false
                        registers[regIndex] = ValueNode(0)
                    } else if (left.range.first == left.range.last && right.range.first == right.range.last &&
                            left.range.first == right.range.first) {
                        // always true
                        registers[regIndex] = ValueNode(1)
                    } else {
                        // split reality! omg
                        return mutableListOf(
                            World(registers.toMutableList(), conditions + "$left == $right", nextVariable)
                                .also { it.registers[regIndex] = ValueNode(1) },
                            World(registers.toMutableList(), conditions + "$left != $right", nextVariable)
                                .also { it.registers[regIndex] = ValueNode(0) }
                        )
                    }
                }
            }

            return listOf(this)
        }

        fun printIt() {
            if (conditions.any()) println("when " + conditions.joinToString(", ") + ":")
            for (i in registers.indices) {
                println("${'w' + i} = ${registers[i]}")
            }
        }

        fun printSmall() {
            println("z = ${registers[3]} when ${conditions.joinToString(", ")}")
        }
    }

    override fun solvePart1(): String {
        var worlds = listOf(World())

        for (line in readGroup()) {
            println()
            println(line)
            val toks = line.split(" ")
            worlds = worlds.flatMap { it.process(toks) }
//            worlds.forEach { it.printIt() }

//            readLine()
        }
        println("There are ${worlds.size} worlds")
        worlds
            .filter {
                val z = it.registers[3]
                if (z is ValueNode) z.value == 0 else false
            }
            .forEach { it.printSmall() }
        return ""
    }
    override val samples = listOf<Sample>()
}