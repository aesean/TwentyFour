package com.aesean.twentyfour

import java.math.BigDecimal

/**
 * Calculate.
 *
 * @author danny
 * @version 1.0
 * @since 6/19/18
 */
interface ICalculateAny {
    fun calculateResult(t: Int, a: Int, b: Int, c: Int, d: Int, callback: (hitCount: Int, allCount: Int, HashSet<String>) -> Unit)
}

class CalculateAnyImpl : ICalculateAny {

    private val mCalculateRule: CalculateRule = CalculateRuleByBigDecimal()
    override fun calculateResult(t: Int, a: Int, b: Int, c: Int, d: Int, callback: (Int, Int, HashSet<String>) -> Unit) {
        val nodes = ArrayList<Node>(4)
        nodes.add(Node(a.toString()))
        nodes.add(Node(b.toString()))
        nodes.add(Node(c.toString()))
        nodes.add(Node(d.toString()))

        fun String.f(): String {
            return String.format("%2.2f", this.toFloat())
        }

        val tree = Tree(nodes, mCalculateRule)
        val result = HashSet<String>()
        var allCount = 0
        var hitCount = 0
        print(mCalculateRule.toString())
        tree.find { it: Node ->
            allCount++
            if ((BigDecimal(it.number) - BigDecimal(t)).abs() <= BigDecimal(mCalculateRule.deviation())) {
                hitCount++
                result.add("${it.desc} = ${it.number.f()}")
            }
        }
        callback.invoke(hitCount, allCount, result)
    }
}