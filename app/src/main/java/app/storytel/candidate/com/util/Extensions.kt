package app.storytel.candidate.com.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders

fun ImageView.loadImage(imageUrl: String) {

    val url = GlideUrl(
        imageUrl, LazyHeaders.Builder()
            .addHeader("User-Agent", "your-user-agent")
            .build())

    Glide.with(this)
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}