package io.github.mkohm.demo.lcom

class HighCohesion {
    private var number = 0
    private var number1 = 0
    private var number2 = 0


    fun inc() {
        number++
        number1++
        number2++
    }

    fun inc2() {
        number++
        number1++
        number2++
    }
}