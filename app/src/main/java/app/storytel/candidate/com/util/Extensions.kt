package app.storytel.candidate.com.util

import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

fun ImageView.loadImage(imageUrl: String, title: TextView, body: TextView? = null) {

    val url = GlideUrl(
        imageUrl, LazyHeaders.Builder()
            .addHeader("User-Agent", "your-user-agent")
            .build())

    Glide.with(this)
        .load(url)
        .listener(object: RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                title.isVisible = false
                body?.isVisible = false
                return false
            }

            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                title.isVisible = true
                body?.isVisible = true
                return false
            }
        })
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}