package virtus.synergy.core

import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase

/**
 * Mind Tempus
 * Created on 29/05/2023.
 * Author: johan
 */

inline fun runCatch(block: () -> Unit) {
    try {
        block()
    } catch (exception: Throwable) {
        exception.logError()
    }
}

inline fun <R> resultCatching(block: () -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (exception: Throwable) {
        exception.logError()
        Result.failure(exception)
    }
}

fun Throwable.logError(message: String = "") {
    Firebase.crashlytics.recordException(
        Exception(message, this)
    )
    this.printStackTrace()
}