package app.storytel.candidate.com.ui.post

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.storytel.candidate.com.databinding.PostItemBinding
import app.storytel.candidate.com.model.Photo
import app.storytel.candidate.com.model.Post
import app.storytel.candidate.com.model.PostAndImages
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import java.util.*
import kotlin.random.Random

class PostAdapter(private val onItemClicked: ((Post, Photo) -> Unit)
): RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    private var mData: PostAndImages = PostAndImages(ArrayList(), ArrayList())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = PostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = mData.posts[position]
        val index = Random.nextInt(mData.photos.size - 1)
        val photo = mData.photos[index]
        holder.bind(post, photo)
        holder.itemView.setOnClickListener {
            onItemClicked(post, photo)
        }
    }

    override fun getItemCount(): Int {
        return mData.posts.size
    }

    fun setData(data: PostAndImages?) {
        if (data != null) {
            mData = data
        }
        notifyDataSetChanged()
    }

    class PostViewHolder(private val binding: PostItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post, photo: Photo) {
            binding.apply {

                val url = GlideUrl(
                    photo.thumbnailUrl, LazyHeaders.Builder()
                        .addHeader("User-Agent", "your-user-agent")
                        .build())

                Glide.with(itemView)
                    .load(url)
                    .into(image)

                title.text = post.title
                body.text = post.body
            }
        }
    }
}