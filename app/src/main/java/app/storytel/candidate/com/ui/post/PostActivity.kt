package app.storytel.candidate.com.ui.post

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import app.storytel.candidate.com.R
import app.storytel.candidate.com.databinding.ActivityPostBinding
import app.storytel.candidate.com.network.response.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostBinding
    private val viewModel: PostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val postAdapter = PostAdapter()

        binding.apply {
            setSupportActionBar(toolbar)

            content.recyclerView.apply {
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
        }
    }

    private fun setLoading(isLoading: Boolean) {
        binding.apply {
            progressBar.isVisible = isLoading
            content.recyclerView.isVisible = !isLoading
        }
    }

    private fun displayError(error: Throwable?) {

        binding.apply {
            errorLayout.isVisible = true
            progressBar.isVisible = false
            content.recyclerView.isVisible = false

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
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }
}