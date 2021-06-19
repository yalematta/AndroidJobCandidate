package app.storytel.candidate.com.ui.details

import android.content.Intent
import androidx.lifecycle.ViewModel
import app.storytel.candidate.com.ui.post.PostActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor() : ViewModel() {

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

    }
}