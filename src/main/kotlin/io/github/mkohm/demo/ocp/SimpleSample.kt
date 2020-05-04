package io.github.mkohm.demo.ocp

class Rectangle(private val width: Int, private val height: Int)
class Circle(private val diameter: Int)

class Drawer {
    fun drawAllShapes() {
        val shapes = Provider.getShapes()

        for (shape in shapes) {
            when (shape) {
                is Rectangle -> drawRectangle(shape)
                is Circle -> drawCircle(shape)
            }
        }
    }

    private fun drawRectangle(shape: Rectangle) {
        println("drawing rectangle")
    }

    private fun drawCircle(shape: Circle) {
        println("drawing circle")
    }
}

object Provider {
    fun getShapes(): List<Any> {
        return listOf(Rectangle(10, 10), Circle(5), Circle(5))
    }
}