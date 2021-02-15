var e = 2

object C {

    var k = 6

    fun foo() {
        var c = 7
    }
}

abstract class A {
    val n = "123"

    abstract fun boo()
}

class B: A() {
    val l = 5
    override fun boo() {
        var m = 123
    }
}