package io.github.mkohm.demo.isp.solution

interface Print {
    fun print()
}

interface Fax {
    fun fax()
}

interface Scan {
    fun scan()
}

class LuxuryPrinter : Print, Scan, Fax {
    override fun print() {
        println("print")
    }

    override fun fax() {
        println("fax")
    }


    override fun scan() {
        println("scan")
    }
}

class EconomicPrinter : Print {
    override fun print() {
        println("print")
    }
}