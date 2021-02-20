
import kastree.ast.Node
import kastree.ast.psi.Parser
import java.io.File

val metrics = Metrics()

fun main(args: Array<String>) {
    val fileName = "src/test/testData/Test1.kt"
    val files = File(fileName)
    val directoryTree = DirectoryTree()
    generateFileTree(files, directoryTree)
    println("Среднее количество переписанных методов для одного kt файла: " + metrics.averageOverriddenMethodsPerFile)
    println("Среднее количество полей для одного класса: " + metrics.averageFieldsPerClass)
    println("Средняя глубина наследования: " + metrics.averageImplementationDepth)
    println("Максимальная глубина наследования: " + metrics.maxImplementationDepth)
    println("A метрика: " + metrics.counterA)
    println("B метрика: " + metrics.counterB)
    println("C метрика: " + metrics.counterC)
}


fun generateFileTree(files: File, directoryTree: DirectoryTree) {
    if (files.isDirectory) {
        directoryTree.name = files.name
        for (file in files.listFiles()) {
            if (file.isFile) {
                val generatedAST = generateAST(file)
                metrics.findAverageOverriddenMethodsPerFile(generatedAST)
                metrics.findAverageFieldsPerClass(generatedAST)
                metrics.findImplementationDepth(generatedAST, file.path)
                metrics.findABCMetric(generatedAST)
                directoryTree.fileASTs.add(generatedAST)
            } else {
                val newDirectory = DirectoryTree()
                generateFileTree(file, newDirectory)
                directoryTree.directories.add(newDirectory)
            }
        }
    } else {
        val generatedAST = generateAST(files)
        metrics.findAverageOverriddenMethodsPerFile(generatedAST)
        metrics.findAverageFieldsPerClass(generatedAST)
        metrics.findImplementationDepth(generatedAST, files.path)
        metrics.findABCMetric(generatedAST)
        directoryTree.fileASTs.add(generatedAST)
    }
}


fun generateAST(code: File): Node.File {
    val codeStr = code.readText().replace("\r\n", "\n")
    return Parser.parseFile(codeStr)
}