import kastree.ast.Node
import kastree.ast.Visitor

class Metrics {

    var averageOverriddenMethodsPerFile = 0.0
    var overrideCounter = 0.0
    var filesCounter = 0.0


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


}