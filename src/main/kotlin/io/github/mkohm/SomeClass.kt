package io.github.mkohm

open class AnotherClass

// This will create a detekt-hint UseCompositionInsteadOfInheritance warning
class SomeClass: AnotherClass()
