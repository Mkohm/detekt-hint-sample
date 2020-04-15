package io.github.mkohm.demo.ocp.solution

interface Shape {
    fun draw()
}

class Rectangle(private val length: Int) : Shape {
    override fun draw() {
        println("drawing rectangle")
    }
}

class Circle(private val diameter: Int) : Shape {
    override fun draw() {
        println("drawing circle")
    }
}

class Draw {
    fun drawAllShapes() {
        val shapes = ShapeProvider.getShapes()

        for (shape in shapes) {
            shape.draw()
        }
    }
}

object ShapeProvider {
    fun getShapes(): List<Shape> {
        return listOf(Rectangle(10), Circle(5), Circle(5))
    }
}