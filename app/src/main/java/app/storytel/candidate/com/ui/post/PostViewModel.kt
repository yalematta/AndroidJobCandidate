package app.storytel.candidate.com.ui.post

import androidx.lifecycle.*
import app.storytel.candidate.com.model.Photo
import app.storytel.candidate.com.model.Post
import app.storytel.candidate.com.model.PostAndImages
import app.storytel.candidate.com.network.ApiService
import app.storytel.candidate.com.network.Repository
import app.storytel.candidate.com.network.response.Result
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

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
                when (val posts = repository.fetchPosts()) {
                    is Result.Success -> {
                        when (val images = repository.fetchPhotos()) {
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

}