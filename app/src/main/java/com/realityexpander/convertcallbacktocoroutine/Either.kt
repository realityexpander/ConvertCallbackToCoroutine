package com.realityexpander.convertcallbacktocoroutine

// Union Class for Booleans, Exceptions, and DataMap (note: specific for docSnapShotMapType)
open class Either private constructor(
    open val boolean: kotlin.Boolean? = null,
    open val dataMap: docSnapShotMapType? = null,
    open val error: Exception? = null
) {
    constructor(value: kotlin.Boolean) : this(boolean = value)
    constructor(value: docSnapShotMapType?) :  this(dataMap = value)
    constructor(value: Exception) : this(error = value)
    constructor(success: kotlin.Boolean, data: docSnapShotMapType?) : this(boolean = success, dataMap = data)

    val isBoolean: kotlin.Boolean
        get() = boolean != null
    val isDataMap: kotlin.Boolean
        get() = dataMap != null
    val isError: kotlin.Boolean
        get() = error != null

    class Boolean(override val boolean: kotlin.Boolean?) : Either(boolean = boolean)
    class DocSnapShotMapType(override val dataMap: docSnapShotMapType?) : Either(dataMap = dataMap)
    class Error(override val error: Exception) : Either(error = error)

    companion object {
        fun boolean(boolean: kotlin.Boolean) = Either(boolean, null, null)
        fun docSnapShotMapType(dataMap: docSnapShotMapType?) = Either(null, dataMap, null)
        fun error(error: Exception) = Either(null, null, error)
    }

    override fun toString(): String {
        return "Either(boolean=$boolean, dataMap=$dataMap, error=$error)"
    }

    fun toBoolean(): kotlin.Boolean {
        return this.boolean ?: false
    }

    fun toDocSnapShotMap(): docSnapShotMapType? {
        return this.dataMap
    }

    fun getItem(key: String): String? {
        return this.dataMap?.get(key)
    }

    fun toError(): Exception {
        return this.error ?: Exception("No Error")
    }
}

fun main() {
    testEither()

    testEither2()
}

fun testEither() {
    println("============ Either ===================")

    val dataMap = Either.docSnapShotMapType(mutableMapOf("key" to "value")).toDocSnapShotMap()

    var either = Either(dataMap)
    println(either)
    println(either.toBoolean())
    println(either.toDocSnapShotMap())

    either = Either(true, dataMap)
    println(either)
    println(either.toBoolean())
    println(either.toDocSnapShotMap())

    either = Either.Boolean(true)
    println(either)
    println(either.toBoolean())
    println(either.toDocSnapShotMap())

    either = Either.DocSnapShotMapType(dataMap)
    println(either)
    println(either.toBoolean())
    println(either.toDocSnapShotMap())

    either = Either.boolean(true)
    println(either)
    println(either.toBoolean())
    println(either.toDocSnapShotMap())

    either = Either.docSnapShotMapType(dataMap)
    println(either)
    println(either.toBoolean())
    println(either.toDocSnapShotMap())

    println("key = " + either.getItem("key"))

    either = Either.error(Exception("Error"))
    println(either)
    println(either.toBoolean())
    println(either.toDocSnapShotMap())
    println(either.toError())

}

// Generic version of the above
private open class Either2<T1,T2>(
    open val typeSimple: T1? = null,
    open val typeComplex: T2? = null,
    open val error: Exception? = null
) {
    constructor(value: T1?, value2: T2? = null) : this(typeSimple = value, typeComplex = null)
    constructor(value: T2?) : this(typeComplex = value)
    constructor(value: Exception) : this(error = value)
//    constructor(value: T2?) :  this(typeComplex = value) // cant define this way (bc T1 & T2 are unknown at compile time)
//    constructor(value: T1?) :  this(typeSimple = value)  // cant define this way

    val isTypeSimple: Boolean
        get() = typeSimple != null
    val isTypeComplex: Boolean
        get() = typeComplex != null
    val isError: Boolean
        get() = error != null

    class TypeSimple<T1,T2>(typeSimple: T1?) : Either2<T1,T2>(typeSimple = typeSimple)
    class TypeComplex<T1,T2>(typeComplex: T2?) : Either2<T1,T2>(typeComplex = typeComplex)
    class Error<T1,T2>(error: Exception) : Either2<T1,T2>(error = error)

    companion object {
        fun boolean(boolean: Boolean) = Either2<Boolean, String>(boolean, null, null)
        fun string(string: String) = Either2<Boolean, String>(null, string, null)
        fun error(error: Exception) = Either2<Boolean, String>(null, null, error)
    }

    override fun toString(): String {
        return "Either(T1=$typeSimple, T2=$typeComplex, error=$error)"
    }

    fun <T1> toT1(): T1? {
        return this.typeSimple as? T1
    }

    fun <T2> toT2(): T2? {
        return this.typeComplex as? T2
    }

    fun toError(): Exception {
        return this.error ?: Exception("No Error")
    }
}

fun testEither2() {
    println()
    println("============ Either2 ===================")

    var either = Either2<Boolean, String>("Hello")
    println(either)
    println(either.toT1<Boolean>())
    println(either.toT2<String>())

    either = Either2<Boolean, String>(true)
    println(either)
    println(either.toT1<Boolean>())
    println(either.toT2<String>())

    either = Either2.TypeSimple<Boolean, String>(true)
    println(either)
    println(either.toT1<Boolean>())
    println(either.toT2<String>())

    either = Either2.TypeComplex<Boolean, String>("Hello")
    println(either)
    println(either.toT1<Boolean>())
    println(either.toT2<String>())

    either = Either2.boolean(true)
    println(either)
    println(either.toT1<Boolean>())
    println(either.toT2<String>())

    either = Either2.string("Hello")
    println(either)
    println(either.toT1<Boolean>())
    println(either.toT2<String>())
}































