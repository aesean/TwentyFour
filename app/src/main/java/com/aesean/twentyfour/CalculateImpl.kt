package com.aesean.twentyfour

import java.math.BigDecimal
import java.util.*


interface MathRule {
    fun size(): Int
    fun calculate(a: String, index: Int, b: String): String
    fun symbol(index: Int): String
    fun deviation(): String
}

fun main(args: Array<String>) {
    test("8,8,3,3")
    test("5,5,5,1")
}

private fun String.format(): String {
    return String.format("%2.1f", this.toFloat())
}

private fun test(s: String) {
    val numbers = s.split(",")
    val nodes = MutableList(numbers.size) {
        Node(numbers[it])
    }
    val tree = Tree(nodes, MathRuleByBigDecimal())
    tree.find {
        if (Math.abs(it.number.toDouble() - 24) < 0.0000000001) {
            println("${it.desc} = ${it.number.format()}")
        }
    }
    println("**********")
    tree.find {
        if (Math.abs(it.number.toDouble()) <= 10) {
            println("${it.desc} = ${it.number.format()}")
        }
    }
}


class MathRuleNormal : MathRule {

    companion object {
        val SYMBOLS = arrayOf("+", "-", "×", "÷")
        const val DEVIATION = "0.00001"
    }

    override fun symbol(index: Int): String {
        return SYMBOLS[index]
    }

    override fun calculate(a: String, index: Int, b: String): String {
        val numA = a.toDouble()
        val numB = b.toDouble()
        when (index) {
            0 -> {
                return (numA + numB).toString()
            }
            1 -> {
                return (numA - numB).toString()
            }
            2 -> {
                return (numA * numB).toString()
            }
            3 -> {
                if (numB == 0.0) {
                    throw RuntimeException("Can't multiply 0")
                }
                return (numA / numB).toString()
            }
            else -> {
                throw RuntimeException("Unknown index")
            }
        }
    }

    override fun size(): Int {
        return SYMBOLS.size
    }

    override fun deviation(): String {
        return DEVIATION
    }

    override fun toString(): String {
        return "MathRuleNormal{SYMBOLS = ${Arrays.toString(SYMBOLS)}, deviation = ${deviation()}}"
    }
}

class MathRuleByBigDecimal : MathRule {

    companion object {
        val SYMBOLS = arrayOf("+", "-", "×", "÷")
        const val DEVIATION = "0.00000000001"
    }

    override fun symbol(index: Int): String {
        return SYMBOLS[index]
    }

    override fun calculate(a: String, index: Int, b: String): String {
        val numA = BigDecimal(a)
        val numB = BigDecimal(b)
        when (index) {
            0 -> {
                return numA.add(numB).toString()
            }
            1 -> {
                return numA.subtract(numB).toString()
            }
            2 -> {
                return numA.multiply(numB).toString()
            }
            3 -> {
                return numA.divide(numB, 16, BigDecimal.ROUND_HALF_UP).toString()
            }
            else -> {
                throw RuntimeException("Unknown index")
            }
        }
    }

    override fun size(): Int {
        return SYMBOLS.size
    }

    override fun deviation(): String {
        return DEVIATION
    }

    override fun toString(): String {
        return "MathRuleByBigDecimal{SYMBOLS = ${Arrays.toString(SYMBOLS)}, deviation = ${deviation()}}"
    }

}

class Tree(private val nodes: MutableList<Node>, private val mathRule: MathRule) {

    private val nodeArrangement = Arrangement(nodes.size)

    fun find(filter: (result: Node) -> Unit) {
        nodeArrangement.reset()
        nodeArrangement.traversal { left: Int, right: Int ->
            val leftNode = nodes[left]
            val rightNode = nodes[right]

            var symbolIndex = 0
            while (symbolIndex < mathRule.size()) {
                val nextNodes: MutableList<Node> = ArrayList(nodes.size - 2)
                nodes.forEachIndexed { index, value ->
                    if ((index != left) and (index != right)) {
                        nextNodes.add(value)
                    }
                }
                val number: String
                try {
                    number = mathRule.calculate(leftNode.number, symbolIndex, rightNode.number)
                } catch (e: Exception) {
                    symbolIndex++
                    continue
                }
                val node = Node(number)
                node.desc = "(${leftNode.desc}${mathRule.symbol(symbolIndex)}${rightNode.desc})"
                nextNodes.add(node)
                if (nextNodes.size > 1) {
                    Tree(nextNodes, mathRule).find(filter)
                } else {
                    val n = nextNodes[0]
                    filter.invoke(n)
                }
                symbolIndex++
            }
        }
    }

    fun size(): Int {
        return nodes.size
    }
}


class Node(var number: String) {
    var desc: String = number

    override fun toString(): String {
        return desc
    }
}

class Arrangement(val size: Int) {
    private var mainIndex: Int = -1
    private var childIndex: Int = -1

    init {
        if (size < 2) {
            throw RuntimeException("size should be >= 2. ")
        }
    }

    fun reset() {
        mainIndex = -1
        childIndex = -1
    }

    private fun next(): Boolean {

        if ((mainIndex == -1) and (childIndex == -1)) {
            mainIndex = 0
            childIndex = 1
            return true
        }

        childIndex++
        var check = false
        if (childIndex < size) {
            check = true
        } else {
            childIndex = 0
            mainIndex++
            if (mainIndex < size) {
                check = true
            }
        }
        if (check) {
            return if (mainIndex == childIndex) {
                next()
            } else {
                true
            }
        }
        return false
    }

    fun traversal(result: (left: Int, right: Int) -> Unit) {
        while (next()) {
            result.invoke(mainIndex, childIndex)
        }
    }
}