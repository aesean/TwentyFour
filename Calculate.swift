//
// Created by Danny Zhang on 6/21/18.
// Copyright (c) 2018 Danny Zhang. All rights reserved.
//

import Foundation

protocol CalculateRule {
    func size() -> Int
    func calculate(a: String, index: Int, b: String) throws -> String
    func symbol(index: Int) -> String
}

class CalculateError: Error {

}

class CalculateRuleImpl: CalculateRule {
    private static let SYMBOLS = ["+", "-", "×", "÷"]

    func size() -> Int {
        return CalculateRuleImpl.SYMBOLS.count
    }

    func calculate(a: String, index: Int, b: String) throws -> String {
        let aNum = NSDecimalNumber(string: a)
        let bNum = NSDecimalNumber(string: b)

        switch index {
        case 0:
            return aNum.adding(bNum).stringValue
        case 1:
            return aNum.subtracting(bNum).stringValue
        case 2:
            return aNum.multiplying(by: bNum).stringValue
        case 3:
            if b == "0" || b == "0.0" {
                throw CalculateError()
            }
            return aNum.dividing(by: bNum).stringValue
        default:
            return ""
        }
    }

    func symbol(index: Int) -> String {
        return CalculateRuleImpl.SYMBOLS[index]
    }
}

class Arrangement {
    private let size: Int
    private var mainIndex = -1
    private var childIndex = -1

    init(_ size: Int) {
        self.size = size
    }

    private func next() -> Bool {
        if mainIndex == -1 && childIndex == -1 {
            mainIndex = 0
            childIndex = 1
            return true
        }
        childIndex += 1
        var check = false
        if childIndex < size {
            check = true
        } else {
            childIndex = 0
            mainIndex += 1
            if mainIndex < size {
                check = true
            }
        }
        if check {
            if mainIndex == childIndex {
                return next()
            } else {
                return true
            }
        }
        return false
    }

    func traversal(_ result: (_ left: Int, _ right: Int) -> Void) {
        mainIndex = -1
        childIndex = -1
        while next() {
            result(mainIndex, childIndex)
        }
    }
}

class Node {
    var number: String
    var desc: String

    init(num: String) {
        number = num
        desc = num
    }
}


class Tree {
    let nodes: [Node]
    let calculateRule: CalculateRule
    let nodeArrangement: Arrangement

    init(nodes: [Node], mathRule: CalculateRule) {
        self.nodes = nodes
        self.calculateRule = mathRule
        self.nodeArrangement = Arrangement(nodes.count)
    }

    func find(filter: (_ result: Node) -> Void) {
        nodeArrangement.traversal({ (left: Int, right: Int) -> Void in
            let leftNode = nodes[left]
            let rightNode = nodes[right]

            for symbolIndex in 0..<calculateRule.size() {
                var result: [Node] = []
                for i in 0..<nodes.count {
                    if i != left && i != right {
                        result.append(nodes[i])
                    }
                }

                var number: String
                do {
                    number = try calculateRule.calculate(a: leftNode.number, index: symbolIndex, b: rightNode.number)
                } catch {
                    continue
                }
                let node = Node(num: number)
                node.desc = "(\(leftNode.desc)\(calculateRule.symbol(index: symbolIndex))\(rightNode.desc))"
                result.append(node)
                if result.count > 1 {
                    Tree(nodes: result, mathRule: calculateRule).find(filter: filter)
                } else {
                    filter(result[0])
                }
            }
        })
    }
}