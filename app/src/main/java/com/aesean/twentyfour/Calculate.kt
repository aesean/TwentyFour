package com.aesean.twentyfour

/**
 * Calculate.
 *
 * @author danny
 * @version 1.0
 * @since 6/19/18
 */
interface ICalculateAny {
    fun calculateResult(t: Int, a: Int, b: Int, c: Int, d: Int): HashSet<String>
}

class CalculateAnyImpl : ICalculateAny {
    override fun calculateResult(t: Int, a: Int, b: Int, c: Int, d: Int): HashSet<String> {
        val nodes = ArrayList<Node>(4)
        nodes.add(Node(a.toString()))
        nodes.add(Node(b.toString()))
        nodes.add(Node(c.toString()))
        nodes.add(Node(d.toString()))

        fun String.f(): String {
            return String.format("%2.2f", this.toFloat())
        }

        val tree = Tree(nodes, MathRuleImpl())
        val result = HashSet<String>()
        tree.math {
            if (Math.abs(it.number.toDouble() - t) < 0.0000000001) {
                result.add("${it.desc} = ${it.number.f()}")
            }
        }
        return result
    }
}