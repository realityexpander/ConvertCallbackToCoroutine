package com.realityexpander.convertcallbacktocoroutine

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