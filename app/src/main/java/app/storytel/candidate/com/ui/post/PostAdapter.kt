package app.storytel.candidate.com.ui.post

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.storytel.candidate.com.databinding.PostItemBinding
import app.storytel.candidate.com.model.Photo
import app.storytel.candidate.com.model.Post
import app.storytel.candidate.com.model.PostAndImages
import com.bumptech.glide.Glide
import kotlin.random.Random
import java.util.*

class PostAdapter(): RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

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
            // add click action
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

                Glide.with(itemView)
                    .load(photo.thumbnailUrl)
                    .into(image)

                title.text = post.title
                body.text = post.body
            }
        }
    }
}