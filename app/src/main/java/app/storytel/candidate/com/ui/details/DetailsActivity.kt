package app.storytel.candidate.com.ui.details

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import app.storytel.candidate.com.databinding.ActivityDetailsBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
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

            val url = GlideUrl(
                viewModel.photoUrl, LazyHeaders.Builder()
                    .addHeader("User-Agent", "your-user-agent")
                    .build())

            Glide.with(this@DetailsActivity)
                .load(url)
                .into(backdrop)
        }

        //TODO display the selected post from ScrollingActivity. Use mImageView and mTextView for image and body text. Change the title to use the post title
        //TODO load top 3 comments from COMMENTS_URL into the 3 card views
    }
}