import kastree.ast.Node
import kastree.ast.Visitor

class Metrics {

    var averageOverriddenMethodsPerFile = 0.0
    var averageFieldsPerClass = 0.0
    var averageImplementationDepth = 0.0
    var maxImplementationDepth = 0
    var counterA = 0
    private val implementationTree = ImplementationTree()
    private var overrideCounter = 0.0
    private var filesCounter = 0.0
    private var counterOfFields = 0.0
    private var counterOfClasses = 0.0
    private val assigmentTokens = mutableSetOf(Node.Expr.BinaryOp.Token.ASSN,
            Node.Expr.BinaryOp.Token.ADD_ASSN, Node.Expr.BinaryOp.Token.DIV_ASSN, Node.Expr.BinaryOp.Token.MOD_ASSN,
            Node.Expr.BinaryOp.Token.MUL_ASSN, Node.Expr.BinaryOp.Token.SUB_ASSN)


    fun findAverageOverriddenMethodsPerFile(fileAst: Node.File) {
        filesCounter++
        Visitor.visit(fileAst) { v, _ ->
            if (v is Node.Decl.Func)
                if (v.mods.isNotEmpty())
                    if (v.mods.filterIsInstance<Node.Modifier.Lit>().firstOrNull()
                            {it.keyword == Node.Modifier.Keyword.OVERRIDE} != null)
                        overrideCounter++
        }
        averageOverriddenMethodsPerFile = overrideCounter / filesCounter
    }

    fun findAverageFieldsPerClass(fileAst: Node.File) {
        Visitor.visit(fileAst) { v, _ ->
            if (v is Node.Decl.Structured)
                if (v.form == Node.Decl.Structured.Form.CLASS) {
                    counterOfClasses++
                    if (v.members.isNotEmpty())
                        counterOfFields += v.members.filterIsInstance<Node.Decl.Property>().size
                }
        }
        averageFieldsPerClass = counterOfFields / counterOfClasses
    }

    fun findImplementationDepth(fileAst: Node.File, fileName: String) {
        val imports = mutableListOf<Node.Import>()
        var pkg: String? = ""
        Visitor.visit(fileAst) { v, _ ->
            if (v is Node.File) {
                pkg = v.pkg?.names?.joinToString(separator = ".")
                if (pkg == null) pkg = fileName
                for (import in v.imports)
                    imports += import
            }
            if (v is Node.Decl.Structured) {
                for (parent in v.parents) {
                    val parentName = Regex("""[a-zA-Z0-9(\[=]+name=|, [ ,a-zA-Z=0-9\[\])]+""")
                            .split(parent.toString())
                    if (parentName.size == 3) {
                        var importPackage = ""
                        var isImported = false
                        for (import in imports)
                            if (import.names.last() == parentName[1]) {
                                importPackage = import.names.joinToString(separator = ".")
                                isImported = true
                            }
                        if (isImported) implementationTree.add(Pair(importPackage, pkg + "." + v.name))
                        else implementationTree.add(Pair(pkg + "." + parentName[1], pkg + "." + v.name))
                    } else implementationTree.add(Pair(parentName.subList(1, parentName.size - 1)
                            .joinToString(separator = "."), pkg + "." + v.name))
                }
            }
        }
        if (implementationTree.size > 0) {
            averageImplementationDepth = implementationTree.size / counterOfClasses
            maxImplementationDepth = implementationTree.findMaxHeight(0) - 2
        }
    }

    fun findAMetric(fileAst: Node.File) {
        Visitor.visit(fileAst) { v, _ ->
            if (v is Node.Decl.Property) {
                counterA++
            }
            if (v is Node.Expr.BinaryOp.Oper.Token) {
                if (assigmentTokens.contains(v.token))
                    counterA++
            }
            if (v is Node.Expr.UnaryOp.Oper) {
                counterA++
            }
        }
    }
}