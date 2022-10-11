package com.realityexpander.convertcallbacktocoroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.FirebaseFirestore
import com.realityexpander.convertcallbacktocoroutine.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val firebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // CALLBACK STYLE
        getDataFromFirestoreUsingCallback(
            { exist ->
                if (exist) {
                    binding.textView.text = "Data Exists"
                } else {
                    binding.textView.text = "Data Not Exist"
                }
            },
            { error ->
                binding.textView.text = "Error: " + error.message
            }
        )

//        // COROUTINES STYLE
        val job = Job()
        val scope = CoroutineScope(Dispatchers.IO + job)
        scope.launch {
            try {
                val result = getDataFromFirestoreUsingSuspend()
//                val result = getDataFromFirestoreUsingSuspendCancellable()
                withContext(Dispatchers.Main) {
                    if (result) {
                        binding.textView2.text = "Data Exists"
                    } else {
                        binding.textView2.text = "Data Not Exist"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    binding.textView2.text = "Error: " + e.message
                }
            }

            // For cancelling the coroutine and use of getDataFromFirestoreUsingSuspendCancellable()
            //job.cancel()
        }

        // COROUTINES STYLE WITH DATA
        val job2 = Job()
        val scope2 = CoroutineScope(Dispatchers.IO + job)
        scope.launch {


            try {
                val result = getDataFromFirestoreUsingSuspendWithData()
                withContext(Dispatchers.Main) {
                    if (result.hasPayload) {
                        result.payload?.let { data ->
                            binding.textView3.text = data.toString() + "\n"+ data["name"]
                        }
                        // binding.textView3.text = result.payload?.get("name") ?: "Data Not Exist" // Direct access to dataMap
                    } else {
                        binding.textView3.text = "Data Not Exist"
                    }
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    binding.textView3.text = "Error: " + e.message
                }
            }
        }
    }

    private fun getDataFromFirestoreUsingCallback(
        onSuccess: (exist: Boolean) -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        firebaseFirestore.collection("users")
            .document("user1")
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    onSuccess(true)
                } else {
                    onSuccess(false)
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    private suspend fun getDataFromFirestoreUsingSuspend(): Boolean { // NOTE: the return value is defined here.
        // Note: the generic type is the successful result type of the Task
        return suspendCoroutine { continuation ->
            firebaseFirestore.collection("users")
                .document("user1")
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        continuation.resume(true) // alternative to send back just a value
//                         continuation.resumeWith(Result.success(true)) // Value in a wrapper (convenient if already a `Result`)
                    } else {
                        //continuation.resume(false) // alternative to send back just a value
//                         continuation.resumeWith(Result.success(false)) // Value in a wrapper (convenient if already a `Result`)
                        continuation.resumeWith(Result.failure(Exception("Data Not Exist"))) // failure must pass an exception
                    }
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }
    }

    private suspend fun getDataFromFirestoreUsingSuspendWithData(): Resource<docSnapShotMapType?> { // NOTE: the return value is defined here.
        return suspendCoroutine { continuation ->
            firebaseFirestore.collection("users")
                .document("user1")
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        continuation.resume(Resource.Success(documentSnapshot.data as? docSnapShotMapType?)) // alternative to send back just a value
                    } else {
                        continuation.resume(Resource.Success(null))
                    }
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }
    }

    // Shows how to implement a cancellable suspend function (but in this case firebase is so fast that we cant cancel the job before its finished)
    private suspend fun getDataFromFirestoreUsingSuspendCancellable(): Boolean { // NOTE: the return value is defined here.
        delay(100)

        // Note: the generic type is the successful return result type of the Task
        return suspendCancellableCoroutine<Boolean> {continuation ->
            firebaseFirestore.collection("users")
                .document("user1")
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        continuation.resume(true) // alternative to send back just a value
                        // continuation.resumeWith(Result.success(true)) // Value in a wrapper (convenient if already a `Result`)
                    } else {
                        continuation.resume(false) // alternative to send back just a value
                        // continuation.resumeWith(Result.failure(Exception("Data Not Exist"))) // Value in a wrapper
                    }
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }

            continuation.invokeOnCancellation {
                // Cancel the task
                continuation.resumeWithException(Exception("Cancelled"))
            }
        }
    }
}




















