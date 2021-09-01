package app.storytel.candidate.com.ui.post

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.storytel.candidate.com.databinding.PostItemBinding
import app.storytel.candidate.com.model.Photo
import app.storytel.candidate.com.model.Post
import app.storytel.candidate.com.model.PostAndImages
import app.storytel.candidate.com.util.loadImage
import java.util.*
import kotlin.random.Random

class PostAdapter(
    private val onItemClicked: ((Post, Photo) -> Unit)
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

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
            val oldPosts = mData.posts
            val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(PostDiffCallback(oldPosts, data.posts))
            mData = data
            diffResult.dispatchUpdatesTo(this)
        }
    }

    class PostViewHolder(private val binding: PostItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post, photo: Photo) {
            binding.apply {

                image.loadImage(photo.thumbnailUrl, title, body)
                title.text = post.title
                body.text = post.body
            }
        }
    }

    class PostDiffCallback(private var oldPosts: List<Post>, private var newPosts: List<Post>) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldPosts.size
        }

        override fun getNewListSize(): Int {
            return newPosts.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldPosts[oldItemPosition].id == newPosts[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldPosts[oldItemPosition] == newPosts[newItemPosition]
        }

    }
}