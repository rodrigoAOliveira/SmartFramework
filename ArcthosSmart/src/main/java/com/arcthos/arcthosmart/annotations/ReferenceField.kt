package com.arcthos.arcthosmart.annotations

@Target(AnnotationTarget.FIELD)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class ReferenceField(val referencedClass : String = "")
