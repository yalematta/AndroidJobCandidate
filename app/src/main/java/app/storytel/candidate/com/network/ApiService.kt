package app.storytel.candidate.com.network

import app.storytel.candidate.com.model.Comment
import app.storytel.candidate.com.model.Photo
import app.storytel.candidate.com.model.Post
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    companion object {
        const val BASE_URL = "https://jsonplaceholder.typicode.com/"
    }

    @GET("posts")
    suspend fun getPosts(): List<Post>

    @GET("photos")
    suspend fun getPhotos(): List<Photo>

    @GET("posts/{id}/comments")
    suspend fun getComments(@Path("id") id: Int): List<Comment>
}