package app.storytel.candidate.com.ui.details

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import app.storytel.candidate.com.R
import app.storytel.candidate.com.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        //TODO display the selected post from ScrollingActivity. Use mImageView and mTextView for image and body text. Change the title to use the post title
        //TODO load top 3 comments from COMMENTS_URL into the 3 card views
    }
}