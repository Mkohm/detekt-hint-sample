package io.github.mkohm.demo.lcom

class LowCohesion {
    private var number0 = 0
    private var number1 = 0
    private var number2 = 0

    fun inc0() {
        number0++
    }

    fun inc1() {
        number1++
    }

    fun inc2() {
        number2++
    }
}
