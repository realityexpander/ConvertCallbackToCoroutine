package com.realityexpander.convertcallbacktocoroutine

// Union Class for Booleans, Exceptions, and DataMap

typealias docSnapShotMapType = MutableMap<String?, String?>

// Resource Class for Booleans, Exceptions, and DataMap
open class Resource<T> private constructor(
    open val payload: T? = null,
    open val error: Exception? = null
) {
    val hasPayload: Boolean
        get() = payload != null
    val isError: Boolean
        get() = error != null

    class Success<T>(data: T) : Resource<T>(payload = data)
    class Failure<T>(override val error: Exception) : Resource<T>(error = error)

    override fun toString(): String {
        return "Resource(hasPayload=$hasPayload, data=$payload, error=$error)"
    }

    fun toError(): Exception {
        return this.error ?: Exception("No Error")
    }
}


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
    class DataMap(override val dataMap: docSnapShotMapType?) : Either(dataMap = dataMap)
    class Error(override val error: Exception) : Either(error = error)

    companion object {
        fun boolean(boolean: kotlin.Boolean) = Either(boolean, null, null)
        fun dataMap(dataMap: docSnapShotMapType?) = Either(null, dataMap, null)
        fun error(error: Exception) = Either(null, null, error)
    }

    override fun toString(): String {
        return "Either(boolean=$boolean, dataMap=$dataMap, error=$error)"
    }

    fun toBoolean(): kotlin.Boolean {
        return this.boolean ?: false
    }

    fun toDataMap(): docSnapShotMapType? {
        return this.dataMap
    }

    fun getItem(key: String): String? {
        return this.dataMap?.get(key)
    }

    fun toError(): Exception {
        return this.error ?: Exception("No Error")
    }
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
//    constructor(value: T2?) :  this(typeComplex = value) // cant define this way
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

fun main() {
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































