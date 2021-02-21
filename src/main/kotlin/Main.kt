import kastree.ast.Node
import kastree.ast.psi.Parser
import java.io.File

val metrics = Metrics()
val generator = XMLGenerator()
val generatedAsts = mutableListOf<Pair<String, Node.File>>()

fun main(args: Array<String>) {
    val files = File(args[0])
    generateFileTree(files)
    findImplementationDepth()
    val metricValues = listOf("${metrics.averageOverriddenMethodsPerClass}", "${metrics.averageFieldsPerClass}",
            "${metrics.averageImplementationDepth}", "${metrics.maxImplementationDepth}", "${metrics.counterA}",
                "${metrics.counterB}", "${metrics.counterC}")
    generator.generateXMLFile(args[1], args[0], metricValues)
    println("Среднее количество переопределённых методов для одного класса: " + metrics.averageOverriddenMethodsPerClass)
    println("Среднее количество полей для одного класса: " + metrics.averageFieldsPerClass)
    println("Средняя глубина наследования: " + metrics.averageImplementationDepth)
    println("Максимальная глубина наследования: " + metrics.maxImplementationDepth)
    println("A метрика: " + metrics.counterA)
    println("B метрика: " + metrics.counterB)
    println("C метрика: " + metrics.counterC)
}


fun generateFileTree(files: File) {
    if (files.isDirectory) {
        for (file in files.listFiles()) {
            if (file.isFile) {
                if (file.name.endsWith(".kt")) {
                    val generatedAST = generateAST(file)
                    generatedAsts.add(Pair(files.path, generatedAST))
                    metrics.findMetrics(generatedAST, files.path)
                }
            } else generateFileTree(file)
        }
    } else {
        if (files.name.endsWith(".kt")) {
            val generatedAST = generateAST(files)
            metrics.findMetrics(generatedAST, files.path)
        }
    }
}

fun findImplementationDepth(){
    for ((key, value) in generatedAsts) {
        metrics.findImplementationDepth(value, key)
    }
}

fun generateAST(code: File): Node.File {
    val codeStr = code.readText().replace("\r\n", "\n")
    return Parser.parseFile(codeStr)
}