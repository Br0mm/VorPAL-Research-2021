var e = 2

object C {

    var k = 6

    fun foo() {
        var c = 7
    }
}

class Test : B() {
    val f = 4
}

abstract class A {
    val n = "123"

    abstract fun boo()
}

open class B: A() {
    val l = 5
    override fun boo() {
        var m = 123
    }
}

class Test2 : A() {
    val e = 6
    override fun boo() {
        val m = 4
    }
}