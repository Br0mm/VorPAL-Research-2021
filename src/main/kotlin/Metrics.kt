
import kastree.ast.Node
import kastree.ast.Visitor

class Metrics {

    var averageOverriddenMethodsPerFile = 0.0
    var averageFieldsPerClass = 0.0
    var averageImplementationDepth = 0.0
    var maxImplementationDepth = 0
    var counterA = 0
    var counterC = 0
    var counterB = 0
    private val implementationTree = ImplementationTree()
    private var overrideCounter = 0.0
    private var filesCounter = 0.0
    private var counterOfFields = 0.0
    private var counterOfClasses = 0.0
    private var declaredStructuresInPackage = mutableMapOf<String?, MutableList<String>>()
    private val assigmentTokens = setOf(Node.Expr.BinaryOp.Token.ASSN,
            Node.Expr.BinaryOp.Token.ADD_ASSN, Node.Expr.BinaryOp.Token.DIV_ASSN, Node.Expr.BinaryOp.Token.MOD_ASSN,
            Node.Expr.BinaryOp.Token.MUL_ASSN, Node.Expr.BinaryOp.Token.SUB_ASSN)
    private val conditionTokens = setOf(Node.Expr.BinaryOp.Token.GT,
            Node.Expr.BinaryOp.Token.GTE, Node.Expr.BinaryOp.Token.EQ, Node.Expr.BinaryOp.Token.NEQ,
            Node.Expr.BinaryOp.Token.LTE, Node.Expr.BinaryOp.Token.LT)


    fun findMetrics(fileAst: Node.File, fileName: String) {
        var pkg: String? = ""
        filesCounter++
        Visitor.visit(fileAst) { v, _ ->
            if (v is Node.File) {
                pkg = v.pkg?.names?.joinToString(separator = ".")
                if (pkg == null) pkg = fileName
                declaredStructuresInPackage[pkg!!] = mutableListOf()
            }
            if (v is Node.Decl.Func)
                if (v.mods.isNotEmpty())
                    if (v.mods.filterIsInstance<Node.Modifier.Lit>().firstOrNull()
                            {it.keyword == Node.Modifier.Keyword.OVERRIDE} != null)
                        overrideCounter++
            if (v is Node.Decl.Structured) {
                declaredStructuresInPackage[pkg]!!.add(v.name)
                if (v.form == Node.Decl.Structured.Form.CLASS) {
                    counterOfClasses++
                    if (v.members.isNotEmpty())
                        counterOfFields += v.members.filterIsInstance<Node.Decl.Property>().size
                }
            }
            if (v is Node.Decl.Property) {
                counterA++
            }
            if (v is Node.Expr.BinaryOp.Oper.Token) {
                if (assigmentTokens.contains(v.token))
                    counterA++
                if (conditionTokens.contains(v.token))
                    counterC++
            }
            if (v is Node.Expr.UnaryOp.Oper) {
                counterA++
            }
            if (v is Node.Expr.Call) {
                counterB++
            }
            if (v is Node.Expr.If) {
                if (v.elseBody != null)
                    counterC++
            }
            if (v is Node.Expr.When) {
                counterC += v.entries.size
            }
            if (v is Node.Expr.Try) {
                counterC += v.catches.size + 1
            }
        }
        averageOverriddenMethodsPerFile = overrideCounter / filesCounter
        averageFieldsPerClass = counterOfFields / counterOfClasses
    }

    fun findImplementationDepth(fileAst: Node.File, fileName: String) {
        val imports = mutableListOf<Node.Import>()
        val children = mutableListOf<String>()
        var pkg: String? = ""
        var ableToFindParentName = false
        Visitor.visit(fileAst) { v, _ ->
            if (v is Node.File) {
                pkg = v.pkg?.names?.joinToString(separator = ".")
                if (pkg == null) pkg = fileName
                for (import in v.imports)
                    imports += import
            }
            if (v is Node.Decl.Structured) {
                if (v.parents.isNotEmpty()) children.add(pkg + "." + v.name)
            }
            if (v is Node.Decl.Structured.Parent) {
                ableToFindParentName = true
            }
            if (v is Node.TypeRef.Simple) {
                if (ableToFindParentName) {
                    ableToFindParentName = false
                    val nameBuilder = StringBuilder()
                    for (piece in v.pieces)
                        nameBuilder.append(piece.name + ".")
                    val name = nameBuilder.toString().removeSuffix(".")
                    if (v.pieces.size == 1) {
                        var importPackage = ""
                        var isImported = false
                        for (import in imports) {
                            if (declaredStructuresInPackage.containsKey(import.names.joinToString(separator = "."))) {
                                for (structure in declaredStructuresInPackage[import.names.joinToString(separator = ".")]!!)
                                if (structure == name) {
                                    importPackage = import.names.joinToString(separator = ".") + "." + name
                                    isImported = true
                                }
                            }
                            if (import.names.last() == name) {
                                importPackage = import.names.joinToString(separator = ".")
                                isImported = true
                            }
                        }
                        if (isImported) implementationTree.add(Pair(importPackage, children.last()))
                        else implementationTree.add(Pair("$pkg.$name", children.last()))
                    } else implementationTree.add(Pair(name, children.last()))
                }
            }
        }
        if (implementationTree.size > 0) {
            averageImplementationDepth = implementationTree.size / counterOfClasses
            maxImplementationDepth = implementationTree.findMaxHeight(0) - 2
        }
    }
}