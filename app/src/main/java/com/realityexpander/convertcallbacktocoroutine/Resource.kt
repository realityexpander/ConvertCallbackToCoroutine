package com.realityexpander.convertcallbacktocoroutine

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

// Resource Class for Booleans, Exceptions, and DataMap
open class Resource<T> private constructor(
    open val payload: T? = null,
    open val error: Exception? = null
) {
    val hasPayload: Boolean
        get() = payload != null
    val isError: Boolean
        get() = error != null

    class Success<T>(data: T?) : Resource<T>(payload = data)
    class Failure<T>(override val error: Exception) : Resource<T>(error = error)

    override fun toString(): String {
        return "Resource(hasPayload=$hasPayload, data=$payload, error=$error)"
    }

    fun toError(): Exception {
        return this.error ?: Exception("No Error")
    }
}


fun main() {
    val data: MutableMap<String, Any> = mutableMapOf("name" to "John", "age" to 30)
    var resourceSuccess = Resource.Success<MutableMap<String, Any>>(data)
    println(resourceSuccess)

    resourceSuccess = Resource.Success<MutableMap<String, Any>>(null)
    println(resourceSuccess)

    val resourceFailure = Resource.Failure<MutableMap<String, Any>>(Exception("Error"))
    println(resourceFailure)
}