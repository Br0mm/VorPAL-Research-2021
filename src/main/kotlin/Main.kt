import kastree.ast.Node
import kastree.ast.psi.Parser
import java.io.File


fun main(args: Array<String>) {

    val fileName = "src/test/testData/"
    val files = File(fileName)
    val fileTree = DirectoryTree()
    if (files.isDirectory)
        generateFileTree(files, fileTree)
}


fun generateFileTree(files: File, directoryTree: DirectoryTree) {
    if (files.isDirectory) {
        directoryTree.name = files.name
        for (file in files.listFiles()) {
            if (file.isFile)
                directoryTree.fileASTs.add(generateAST(file))
            else {
                val newDirectory = DirectoryTree()
                generateFileTree(file, newDirectory)
                directoryTree.directories.add(newDirectory)
            }
        }
    }
}


fun generateAST(code: File): Node.File {
    val codeStr = code.readText().replace("\r\n", "\n")
    return Parser.parseFile(codeStr)
}