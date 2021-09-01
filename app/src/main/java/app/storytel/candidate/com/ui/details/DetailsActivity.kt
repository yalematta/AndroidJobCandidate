package app.storytel.candidate.com.ui.details

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import app.storytel.candidate.com.databinding.ActivityDetailsBinding
import app.storytel.candidate.com.model.Comment
import app.storytel.candidate.com.network.response.Result
import app.storytel.candidate.com.ui.post.PostActivity.Companion.PHOTO_URL
import app.storytel.candidate.com.ui.post.PostActivity.Companion.POST_BODY
import app.storytel.candidate.com.ui.post.PostActivity.Companion.POST_ID
import app.storytel.candidate.com.ui.post.PostActivity.Companion.POST_TITLE
import app.storytel.candidate.com.util.loadImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private var _binding: ActivityDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.init(intent)

        binding.apply {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = viewModel.postTitle
            swipeLayout.isEnabled = false
        }

        savedInstanceState?.let { inState ->
            (inState[POST_ID] as Int).let { postId ->
                viewModel.postId = postId
            }
            (inState[PHOTO_URL] as String).let { photoUrl ->
                viewModel.photoUrl = photoUrl
            }
            (inState[POST_TITLE] as String).let { postTitle ->
                viewModel.postTitle = postTitle
            }
            (inState[POST_BODY] as String).let { postBody ->
                viewModel.postBody = postBody
            }
        }

        getComments()
    }

    private fun getComments() {
        viewModel.getComments().observe(this@DetailsActivity, { result ->

            showLoadingIndicator(result is Result.Loading)

            when (result) {
                is Result.Success -> showComments(result.data)
                is Result.Failure -> displayError(result.error)
                else -> showLoadingIndicator(result is Result.Loading)
            }

        })
    }

    private fun showComments(comments: List<Comment>) {
        binding.apply {
            comment1.isVisible = comments.isNotEmpty()
            comment2.isVisible = comments.size > 1
            comment3.isVisible = comments.size > 2
            noComment.isVisible = comments.isEmpty()

            if (comments.isNotEmpty()) {

                title1.text = comments[0].name
                description1.text = comments[0].body
                if (comments.size > 1) {
                    title2.text = comments[1].name
                    description2.text = comments[1].name
                    if (comments.size > 2) {
                        title3.text = comments[2].name
                        description3.text = comments[2].name
                    }
                }
            }

            viewModel.photoUrl?.let { backdrop.loadImage(it, details) }
            details.text = viewModel.postBody
        }
    }

    private fun showLoadingIndicator(show: Boolean) {
        binding.apply {
            commentsView.isVisible = !show
            swipeLayout.isRefreshing = show
        }
    }

    private fun retryLoading() {
        viewModel.postId?.let {
            viewModel.loadData(it)
        } ?: run {
            getComments()
        }
        binding.apply {
            errorLayout.isVisible = false
            commentsView.isVisible = true
        }
    }

    private fun displayError(error: Throwable?) {

        binding.apply {

            errorLayout.isVisible = true
            commentsView.isVisible = false
            swipeLayout.isRefreshing = false

            errorMessage.text = error?.message

            retryButton.setOnClickListener {
                retryLoading()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(POST_ID, viewModel.postId!!)
        outState.putString(PHOTO_URL, viewModel.photoUrl!!)
        outState.putString(POST_TITLE, viewModel.postTitle!!)
        outState.putString(POST_BODY, viewModel.postBody!!)
    }
}