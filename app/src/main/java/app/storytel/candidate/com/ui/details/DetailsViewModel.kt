package app.storytel.candidate.com.ui.details

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.storytel.candidate.com.model.Comment
import app.storytel.candidate.com.network.Repository
import app.storytel.candidate.com.network.response.Result
import app.storytel.candidate.com.ui.post.PostActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val comments = MutableLiveData<Result<List<Comment>>>().apply {
        value = Result.Loading<List<Comment>>(null)
    }

    var postId: Int? = null
    var postTitle: String? = null
    var postBody: String? = null
    var photoUrl: String? = null

    fun init(intent: Intent) {

        if (intent.hasExtra(PostActivity.POST_ID)) {
            postId = intent.getIntExtra(PostActivity.POST_ID, 0)
        }

        if (intent.hasExtra(PostActivity.POST_TITLE)) {
            postTitle = intent.getStringExtra(PostActivity.POST_TITLE)
        }

        if (intent.hasExtra(PostActivity.POST_BODY)) {
            postBody = intent.getStringExtra(PostActivity.POST_BODY)
        }

        if (intent.hasExtra(PostActivity.PHOTO_URL)) {
            photoUrl = intent.getStringExtra(PostActivity.PHOTO_URL)
        }

        postId?.let {
            loadData(it)
        }
    }

    fun getComments(): LiveData<Result<List<Comment>>> {
        return comments
    }

    fun loadData(postId: Int) {
        viewModelScope.launch {
            comments.postValue(Result.Loading<List<Comment>>(null))
            val response = repository.fetchComments(postId)
            comments.postValue(response)
        }
    }
}