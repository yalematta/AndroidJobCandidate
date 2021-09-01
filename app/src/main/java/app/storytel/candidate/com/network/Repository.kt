package app.storytel.candidate.com.network

import app.storytel.candidate.com.model.Comment
import app.storytel.candidate.com.model.Photo
import app.storytel.candidate.com.model.Post
import app.storytel.candidate.com.network.response.Result
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(private val apiService: ApiService) {

    suspend fun fetchPosts(): Result<List<Post>> {
        return try {
            val response = apiService.getPosts()
            Result.Success(response)
        } catch (ex: Exception) {
            Result.Failure(ex)
        }
    }

    suspend fun fetchPhotos(): Result<List<Photo>> {
        return try {
            val response = apiService.getPhotos()
            Result.Success(response)
        } catch (ex: Exception) {
            Result.Failure(ex)
        }
    }

    suspend fun fetchComments(postId: Int): Result<List<Comment>> {
        return try {
            val response = apiService.getComments(postId)
            Result.Success(response)
        } catch (ex: Exception) {
            Result.Failure(ex)
        }
    }
}