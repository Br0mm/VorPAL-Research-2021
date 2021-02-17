class ImplementationTree {

    private class Node {
        val children: MutableList<Node> = mutableListOf()
        var parent: Node? = null
        var value: String? = null
    }

    private var root = Node()

    var size: Int = 0
        private set

    fun clear() {
        root.children.clear()
        size = 0
    }

    fun add(element: Pair<String, String>) {
        size++
        if (runThroughAllChildren(root, element))
            return
        val newParent = Node()
        newParent.value = element.first
        newParent.parent = root
        root.children.add(newParent)
        val newChild = Node()
        newChild.value = element.second
        newChild.parent = newParent
        newParent.children.add(newChild)
        for (child in root.children) {
            if (child.value == element.second) {
                child.parent = newParent
                newParent.children[0] = child
                root.children.remove(child)
            }
        }
    }

    private fun runThroughAllChildren(node: Node, element: Pair<String, String>): Boolean {
        var nodeWereAdded = false
        for(child in node.children) {
            if (child.value == element.first) {
                val newNode = Node()
                newNode.value = element.second
                newNode.parent = child
                child.children.add(newNode)
                nodeWereAdded = true
            }
            if (child.children.size > 0) runThroughAllChildren(child, element)
        }
        return nodeWereAdded
    }
}