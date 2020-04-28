package io.github.mkohm.demo.isp

interface AllInOnePrinter {
    fun print()
    fun fax()
    fun scan()
}

class LuxuryPrinter : AllInOnePrinter {
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


class EconomicPrinter : AllInOnePrinter {
    override fun print() {
        println("I can only print")
    }

    override fun fax() {
        // Unsupported
    }

    override fun scan() {
        // Unsupported
    }
}