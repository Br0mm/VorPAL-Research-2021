import kastree.ast.Node
import kastree.ast.Visitor

class Metrics {

    var averageOverriddenMethodsPerFile = 0.0
    var averageFieldsPerClass = 0.0
    private var overrideCounter = 0.0
    private var filesCounter = 0.0
    private var counterOfFields = 0.0
    private var counterOfClasses = 0.0


    fun findAverageOverriddenMethodsPerFile(fileAST: Node.File) {
        filesCounter++
        Visitor.visit(fileAST) { v, _ ->
            if (v is Node.Decl.Func)
                if (v.mods.isNotEmpty())
                    if (v.mods.filterIsInstance<Node.Modifier.Lit>().firstOrNull() {it.keyword == Node.Modifier.Keyword.OVERRIDE} != null)
                        overrideCounter++
        }
        averageOverriddenMethodsPerFile = overrideCounter / filesCounter
    }

    fun findAverageFieldsPerClass(fileAST: Node.File) {
        Visitor.visit(fileAST) { v, _ ->
            if (v is Node.Decl.Structured)
                if (v.form == Node.Decl.Structured.Form.CLASS) {
                    counterOfClasses++
                    if (v.members.isNotEmpty())
                        counterOfFields += v.members.filterIsInstance<Node.Decl.Property>().size
                }
        }
        averageFieldsPerClass = counterOfFields / counterOfClasses
    }
}