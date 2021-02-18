var e = 2

object C {

    var k = 6

    fun foo() {
        var c = 7
    }
}

class T : B() {
    var f = 4
    var k = 7
    fun foo() {
        k = 8
        k += 3
        k++
    }
}

abstract class Q {
    val n = "123"

    abstract fun boo()
}

open class B: Q() {
    val l = 5
    override fun boo() {
        var m = 123
    }
}

class Test2 : Q() {
    val e = 6
    override fun boo() {
        val m = 4
    }
}