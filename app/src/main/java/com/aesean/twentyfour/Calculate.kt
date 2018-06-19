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
        val tree = Tree(nodes, MathRuleImpl())
        val result = HashSet<String>()
        tree.math {
            if ((it.number == t.toString()) or (it.number == "$t.0")) {
                result.add("${it.desc} = ${it.number}")
            }
        }
        return result
    }
}