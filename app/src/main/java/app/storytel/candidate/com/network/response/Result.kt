package app.storytel.candidate.com.network.response

sealed class Result<out T: Any> {
    data class Success<out T: Any>(val data: T): Result<T>()
    data class Loading<out T: Any>(val data: T? = null) : Result<T>()
    data class Failure(val error: Throwable?): Result<Nothing>()
}