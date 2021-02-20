package directory4

var b = "123"

class Test {
    var f = 4
    var k = 7
    fun foo(): Int {
        if (f > k){
            k = f
        } else f = k
        if (f < k){
            k = f
        }
        var l = k < f
        l = k == f
        l = k >= f
        l = k != f
        l = k <= f
        when {
            k == 4 -> l = false
            f == 1 -> l = true
            else -> l = true
        }
        when(k) {
            1 -> f = 4
            2 -> l = false
            else -> l = true
        }

        try {
            l = true
        } catch (e: Exception) {

        }

        k = if (l) 7
        else 4
        return 10
    }
}
