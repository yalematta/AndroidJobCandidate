package app.storytel.candidate.com.ui.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.storytel.candidate.com.model.Photo
import app.storytel.candidate.com.model.Post
import app.storytel.candidate.com.model.PostAndImages
import app.storytel.candidate.com.network.ApiService
import app.storytel.candidate.com.network.response.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(private val apiService: ApiService) : ViewModel() {

    private val postsAndImages = MutableLiveData<Result<PostAndImages>>().apply {
        this.value = Result.Loading<PostAndImages>(null)
    }

    fun getPostsAndImages(): LiveData<Result<PostAndImages>> {
        if (postsAndImages.value == Result.Loading<PostAndImages>(null)) {
            loadData()
        }
        return postsAndImages
    }

    fun loadData() {
        viewModelScope.launch {
            try {
                postsAndImages.postValue(Result.Loading<PostAndImages>( null))
                when (val posts = fetchPosts()) {
                    is Result.Success -> {
                        when (val images = fetchPhotos()) {
                            is Result.Success -> postsAndImages.postValue(
                                Result.Success(PostAndImages(posts.data, images.data))
                            )
                            is Result.Failure -> postsAndImages.postValue(images)
                        }
                    }
                    is Result.Failure -> postsAndImages.postValue(posts)
                }
            } catch (e: Exception) {
                postsAndImages.postValue(Result.Failure(e))
            }
        }
    }

    private suspend fun fetchPosts(): Result<List<Post>> {
        return try {
            val response = apiService.getPosts()
            Result.Success(response)
        } catch (ex: Exception) {
            Result.Failure(ex)
        }
    }

    private suspend fun fetchPhotos(): Result<List<Photo>> {
        return try {
            val response = apiService.getPhotos()
            Result.Success(response)
        } catch (ex: Exception) {
            Result.Failure(ex)
        }
    }

}