import kastree.ast.Node

class DirectoryTree {

    var name: String = ""
    val directories: MutableList<DirectoryTree> = mutableListOf()
    val fileASTs: MutableList<Node.File> = mutableListOf()
}