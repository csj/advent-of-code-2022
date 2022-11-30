package puzzles

import DailyPuzzle
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import run

suspend fun main() = Day18().run()

class Day18: DailyPuzzle(18) {
    abstract class Node {
        abstract fun asString(depth: Int = 0): String
        abstract fun addToChild(isLeft: Boolean, addValue: Int): Node
        abstract fun explode(depth: Int = 0): Triple<Node, Int, Int>?
        abstract fun split(): Node?
        abstract fun splitFully(): Node?
        abstract fun magnitude(): Int

        fun processFully(): Node {
            val processed = explode()?.first ?: splitFully() ?: return this
            return processed.processFully()
        }
        operator fun plus(other: Node) = BranchNode(this, other).processFully()
    }

    data class ValueNode(val value: Int): Node() {
        override fun toString() = asString()
        override fun asString(depth: Int) = value.toString()

        override fun magnitude() = value
        override fun explode(depth: Int) = null
        override fun addToChild(isLeft: Boolean, addValue: Int) = ValueNode(value + addValue)
        override fun split(): Node? =
            if (value >= 10) BranchNode(ValueNode(value / 2), ValueNode((value + 1) / 2))
            else null
        override fun splitFully() = split()
    }

    data class BranchNode(val left: Node, val right: Node): Node() {
        override fun toString() = asString()
        override fun asString(depth: Int): String {
            val (l,r) = if (depth < 4) Pair("[", "]") else Pair("<", ">")
            return "$l${left.asString(depth+1)},${right.asString(depth+1)}$r"
        }
        override fun magnitude() = 3*left.magnitude() + 2*right.magnitude()
        override fun splitFully(): Node? {
            left.splitFully()?.let { return BranchNode(it, right) }
            right.splitFully()?.let { return BranchNode(left, it) }
            return null
        }

        override fun explode(depth: Int): Triple<Node, Int, Int>? {
            if (depth == 4) return Triple(ValueNode(0), (left as ValueNode).value, (right as ValueNode).value)
            left.explode(depth + 1)?.let { (newNode, leftValue, rightValue) ->
                return Triple(BranchNode(newNode, right.addToChild(true, rightValue)), leftValue, 0)
            }
            right.explode(depth + 1)?.let { (newNode, leftValue, rightValue) ->
                return Triple(BranchNode(left.addToChild(false, leftValue), newNode), 0, rightValue)
            }
            return null
        }

        override fun addToChild(isLeft: Boolean, addValue: Int): Node = BranchNode(
            if (isLeft) left.addToChild(true, addValue) else left,
            if (!isLeft) right.addToChild(false, addValue) else right
        )

        override fun split(): Node? {
            left.split()?.let { return BranchNode(it, right) }
            right.split()?.let { return BranchNode(left, it) }
            return null
        }
    }

    private fun parse(input: String): Node {
        var pos = 0
        fun parseInternal(): Node = when (input[pos]) {
            '[', '<' -> {
                pos++  // '['
                val left = parseInternal()
                pos++  // ','
                val right = parseInternal()
                pos++  // ']'
                BranchNode(left, right)
            }
            else -> {
                var value = 0
                while (pos < input.length && input[pos].isDigit()) {
                    value = value * 10 + input[pos].toString().toInt()
                    pos++
                }
                ValueNode(value)
            }
        }
        return parseInternal()
    }
    private fun sum(nodes: List<Node>): Node = nodes.reduce { a, b -> a + b }

    @TestFactory
    fun testMagnitudes() =
        listOf(
            "[[[[6,6],[7,6]],[[7,7],[7,0]]],[[[7,7],[7,7]],[[7,8],[9,9]]]]" to 4140,
            "[[1,2],[[3,4],5]]" to 143,
            "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]" to 1384,
            "[[[[1,1],[2,2]],[3,3]],[4,4]]" to 445,
            "[[[[3,0],[5,3]],[4,4]],[5,5]]" to 791,
            "[[[[5,0],[7,4]],[5,5]],[6,6]]" to 1137,
            "[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]" to 3488
        ).map { (input, expected) ->
            dynamicTest("$input -> $expected") {
                parse(input).magnitude() shouldBe expected
            }
        }

    @TestFactory
    fun testSums() =
        listOf(
            listOf("[1,1]", "[2,2]", "[3,3]", "[4,4]") to "[[[[1,1],[2,2]],[3,3]],[4,4]]",
            listOf("[1,1]", "[2,2]", "[3,3]", "[4,4]", "[5,5]") to "[[[[3,0],[5,3]],[4,4]],[5,5]]",
            listOf("[1,1]", "[2,2]", "[3,3]", "[4,4]", "[5,5]", "[6,6]") to "[[[[5,0],[7,4]],[5,5]],[6,6]]",
            listOf(
                "[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]",
                "[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]",
                "[[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]",
                "[[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]",
                "[7,[5,[[3,8],[1,4]]]]",
                "[[2,[2,2]],[8,[8,1]]]",
                "[2,9]",
                "[1,[[[9,3],9],[[9,0],[0,7]]]]",
                "[[[5,[7,4]],7],1]",
                "[[[[4,2],2],6],[8,7]]",
            ) to "[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]",
            listOf(
                "[[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]",
                "[[[5,[2,8]],4],[5,[[9,9],0]]]",
                "[6,[[[6,2],[5,6]],[[7,6],[4,7]]]]",
                "[[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]",
                "[[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]",
                "[[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]",
                "[[[[5,4],[7,7]],8],[[8,3],8]]",
                "[[9,3],[[9,9],[6,[4,9]]]]",
                "[[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]",
                "[[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]",
            ) to "[[[[6,6],[7,6]],[[7,7],[7,0]]],[[[7,7],[7,7]],[[7,8],[9,9]]]]"
        ).map { (input, expected) ->
            dynamicTest("Summing $input") {
                sum(input.map { parse(it) }).asString() shouldBe parse(expected).asString()
            }
        }

    override fun solvePart1() = sum(readGroup().map { parse(it) }).magnitude().toString()
    override fun solvePart2(): String {
        val nodes = readGroup().map { parse(it) }
        val maxMagnitude = nodes.maxOf { node1 ->
            nodes.filterNot { it == node1 }.maxOf { node2 ->
                (node1 + node2).magnitude()
            }
        }
        return maxMagnitude.toString()
    }
    override val sampleInput = """
        [[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]
        [[[5,[2,8]],4],[5,[[9,9],0]]]
        [6,[[[6,2],[5,6]],[[7,6],[4,7]]]]
        [[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]
        [[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]
        [[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]
        [[[[5,4],[7,7]],8],[[8,3],8]]
        [[9,3],[[9,9],[6,[4,9]]]]
        [[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]
        [[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]
    """.trimIndent()
    override val sampleAnswerPart1 = "4140"
    override val sampleAnswerPart2 = "3993"
}