package com.aesean.twentyfour

import java.math.BigDecimal

interface MathRule {
    fun size(): Int
    fun math(a: String, index: Int, b: String): String
    fun symbol(index: Int): String
}

fun main(args: Array<String>) {
    val source = "1,5,5,5"
    val numbers = source.split(",")

    val nodes = MutableList(numbers.size) {
        Node(numbers[it])
    }
    val tree = Tree(nodes, MathRuleImpl())
    tree.math {
        if ((it.number == "24.0") or (it.number == "24")) {
            println("${it.desc} = ${it.number}")
        }
    }
    println("**********")
    tree.math {
        if (Math.abs(it.number.toDouble()) <= 10) {
            println("${it.desc} = ${it.number}")
        }
    }
}


class MathRuleImpl : MathRule {

    companion object {
        val SYMBOLS = arrayOf("+", "-", "×", "÷")
    }

    override fun symbol(index: Int): String {
        return SYMBOLS[index]
    }

    override fun math(a: String, index: Int, b: String): String {
        val numA = BigDecimal(a)
        val numB = BigDecimal(b)
        when (index) {
            0 -> {
                return numA.plus(numB).toString()
            }
            1 -> {
                return numA.minus(numB).toString()
            }
            2 -> {
                return numA.multiply(numB).toString()
            }
            3 -> {
                return numA.divide(numB).toString()
            }
            else -> {
                throw RuntimeException("Unknown index")
            }
        }
    }

    override fun size(): Int {
        return SYMBOLS.size
    }

}

class Tree(private val nodes: MutableList<Node>, private val mathRule: MathRule) {

    private val nodeArrangement = Arrangement(nodes.size)

    fun math(filter: (result: Node) -> Unit) {
        nodeArrangement.reset()
        nodeArrangement.traversal { left: Int, right: Int ->
            val leftNode = nodes[left]
            val rightNode = nodes[right]

            var symbolIndex = 0
            while (symbolIndex < mathRule.size()) {
                val result: MutableList<Node> = ArrayList(nodes.size - 2)
                nodes.forEachIndexed { index, value ->
                    if ((index != left) and (index != right)) {
                        result.add(value)
                    }
                }
                val number: String
                try {
                    number = mathRule.math(leftNode.number, symbolIndex, rightNode.number)
                } catch (e: Exception) {
                    symbolIndex++
                    continue
                }
                val node = Node(number)
                node.desc = "(${leftNode.desc}${mathRule.symbol(symbolIndex)}${rightNode.desc})"
                result.add(node)
                if (result.size > 1) {
                    Tree(result, mathRule).math(filter)
                } else {
                    val n = result[0]
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