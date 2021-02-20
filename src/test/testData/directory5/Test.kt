package directory5

class Test {
    var f = 4
    var k = 7
    fun foo(): Int {
        return 10
    }

    fun boo() {
        f = 6
    }

    fun loo(): Boolean {
        return true
    }

    fun test() {
        var l = false
        l = loo()
        var m = foo()
        boo()
    }
}

class Test2 {
    val p = Test()
    var v = p.foo()
    var n = p.loo()
}