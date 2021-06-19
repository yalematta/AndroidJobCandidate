package app.storytel.candidate.com.ui.details

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import app.storytel.candidate.com.databinding.ActivityDetailsBinding
import app.storytel.candidate.com.model.Comment
import app.storytel.candidate.com.network.response.Result
import app.storytel.candidate.com.util.loadImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private val viewModel: DetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.init(intent)

        binding.apply {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = viewModel.postTitle
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

            viewModel.photoUrl?.let { backdrop.loadImage(it) }
            details.text = viewModel.postBody
        }
    }

    private fun showLoadingIndicator(show: Boolean) {
        binding.apply {
            commentsView.isVisible = !show
            progressBar.isVisible = show
        }
    }

    private fun displayError(error: Throwable?) {

        binding.apply {

            commentsView.isVisible = false
            progressBar.isVisible = false
            errorLayout.isVisible = true

            errorMessage.text = error?.message

            retryButton.setOnClickListener {
                viewModel.postId?.let {
                    viewModel.loadData(it)
                } ?: run {
                    getComments()
                }
                errorLayout.isVisible = false
                commentsView.isVisible = true
            }
        }
    }
}