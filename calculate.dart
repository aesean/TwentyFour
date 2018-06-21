abstract class CalculateRule {
//  fun size(): Int
//  fun calculate(a: String, index: Int, b: String): String
//  fun symbol(index: Int): String
//  fun deviation(): String
  int size();

  String calculate(String a, int index, String b);

  String symbol(int index);

  String deviation();
}

class CalculateRuleImpl implements CalculateRule {
  static const _SYMBOL = const ["+", "-", "×", "÷"];

  @override
  String calculate(String a, int index, String b) {
    var numA = double.parse(a);
    var numB = double.parse(b);
    switch (index) {
      case 0:
        return (numA + numB).toString();
      case 1:
        return (numA - numB).toString();
      case 2:
        return (numA * numB).toString();
      case 3:
        if (numB == 0) {
          return null;
        }
        return (numA / numB).toString();
      default:
        return null;
    }
  }

  @override
  String deviation() {
    return "0.00001";
  }

  @override
  int size() {
    return _SYMBOL.length;
  }

  @override
  String symbol(int index) {
    return _SYMBOL[index];
  }
}

class Node {
  String number;
  String desc;

  Node(this.number) {
    this.desc = this.number;
  }
}

class Arrangement {
  int size;
  int _mainIndex = -1;
  int _childIndex = -1;

  Arrangement(this.size) {
    if (this.size < 2) {
      throw new ArgumentError.value(this.size, "size should be >= 2.");
    }
  }

  bool _next() {
    if ((_mainIndex == -1) && (_childIndex == -1)) {
      _mainIndex = 0;
      _childIndex = 1;
      return true;
    }

    _childIndex++;
    var check = false;
    if (_childIndex < size) {
      check = true;
    } else {
      _childIndex = 0;
      _mainIndex++;
      if (_mainIndex < size) {
        check = true;
      }
    }
    if (check) {
      if (_mainIndex == _childIndex) {
        return _next();
      } else {
        return true;
      }
    }
    return false;
  }

  void traversal(void f(int left, int right)) {
    _mainIndex = -1;
    _childIndex = -1;
    while (_next()) {
      f(_mainIndex, _childIndex);
    }
  }
}

class Tree {
  List<Node> _nodes;
  CalculateRule _calculateRule;
  Arrangement _nodeArrangement;

  Tree(this._nodes, this._calculateRule) {
    _nodeArrangement = new Arrangement(_nodes.length);
  }

  void find(void f(Node node)) {
    _nodeArrangement.traversal((left, right) {
      var leftNode = _nodes[left];
      var rightNode = _nodes[right];

      for (var symbolIndex = 0;
      symbolIndex < _calculateRule.size();
      symbolIndex++) {
        var nextNodes = new List<Node>();
        for (var i = 0; i < _nodes.length; i++) {
          if (i != left && i != right) {
            nextNodes.add(_nodes[i]);
          }
        }

        var number = _calculateRule.calculate(
            leftNode.number, symbolIndex, rightNode.number);
        if (number == null) {
          continue;
        }

        var node = new Node(number);
        node.desc =
        "(${leftNode.desc}${_calculateRule.symbol(symbolIndex)}${rightNode
            .desc})";
        nextNodes.add(node);
        if (nextNodes.length > 1) {
          new Tree(nextNodes, _calculateRule).find(f);
        } else {
          f(nextNodes[0]);
        }
      }
    });
  }

  int size() {
    return _nodes.length;
  }
}

void main() {
  test("3,3,8,8");
  test("5,5,5,1");
  test("3,4,5,6");
  test("4,4,7,7");
  test("1,10,500,2000");
}

void test(String source) {
  var split = source.split(",");
  var nodes = new List<Node>();
  for (var value in split) {
    nodes.add(new Node(value));
  }
  var start = new DateTime.now();
  var _calculateRule = new CalculateRuleImpl();
  print("Leght = ${nodes.length}");
  var tree = new Tree(nodes, _calculateRule);
  var t = double.parse("24");
  var deviation = double.parse(_calculateRule.deviation());
  var s = "";
  var allCount = 0;
  var hitCount = 0;
  tree.find((node) {
    allCount++;
    var diff = double.parse(node.number) - t;
    if (diff < 0.000001 && diff > -deviation) {
      hitCount++;
      var info = node.desc + " = " + node.number + "\n";
      if (!s.contains(info)) {
        s += info;
      }
    }
  });
  var end = new DateTime.now();
  var time = end.millisecond - start.millisecond;
  print("----$hitCount/$allCount/$time----\n" + s);
}
