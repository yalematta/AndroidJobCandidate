package app.storytel.candidate.com.ui.post

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import app.storytel.candidate.com.R
import app.storytel.candidate.com.databinding.ActivityPostBinding
import app.storytel.candidate.com.network.response.Result
import app.storytel.candidate.com.ui.details.DetailsActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostActivity : AppCompatActivity() {

    private var _binding: ActivityPostBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val postAdapter = PostAdapter(onItemClicked = { post, photo ->
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra(POST_ID, post.id)
            intent.putExtra(POST_TITLE, post.title)
            intent.putExtra(POST_BODY, post.body)
            intent.putExtra(PHOTO_URL, photo.url)
            startActivity(intent)
        })

        binding.apply {
            setSupportActionBar(toolbar)

            content.recyclerView.apply {
                setHasFixedSize(true)
                adapter = postAdapter
                layoutManager = LinearLayoutManager(this@PostActivity)
            }

            viewModel.getPostsAndImages().observe(this@PostActivity, { result ->

                setLoading(result is Result.Loading)

                when (result) {
                    is Result.Failure -> displayError(result.error)
                    is Result.Success -> {
                        postAdapter.setData(result.data)
                        errorLayout.isVisible = false
                    }
                    else ->  setLoading(result is Result.Loading)
                }
            })

            content.swipeLayout.setOnRefreshListener {
                viewModel.loadData()
            }
        }
    }

    private fun setLoading(isLoading: Boolean) {
        binding.apply {
            content.swipeLayout.isRefreshing = isLoading
            content.recyclerView.isVisible = !isLoading
        }
    }

    private fun displayError(error: Throwable?) {

        binding.apply {
            errorLayout.isVisible = true
            content.recyclerView.isVisible = false
            content.swipeLayout.isRefreshing = false

            errorMessage.text = error?.message

            retryButton.setOnClickListener {
                viewModel.loadData()
                errorLayout.isVisible = false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return if (id == R.id.action_refresh) {
            viewModel.loadData()
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.content.recyclerView.adapter = null
        _binding = null
    }

    companion object {
        const val POST_ID = "POST_ID"
        const val POST_TITLE = "POST_TITLE"
        const val POST_BODY = "POST_BODY"
        const val PHOTO_URL = "PHOTO_URL"
    }
}