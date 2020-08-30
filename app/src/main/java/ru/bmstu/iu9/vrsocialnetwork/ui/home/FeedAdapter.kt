package ru.bmstu.iu9.vrsocialnetwork.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.bmstu.iu9.vrsocialnetwork.R
import ru.bmstu.iu9.vrsocialnetwork.data.model.Post

class FeedAdapter(private val posts: ArrayList<Post>): RecyclerView.Adapter<FeedHolder>() {
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedHolder =
		FeedHolder(
			LayoutInflater.from(parent.context).inflate(R.layout.post, parent, false)
		)

	override fun onBindViewHolder(holder: FeedHolder, position: Int) {
		holder.bind(post = posts[position])
	}

	override fun getItemCount(): Int = posts.size

	fun addPosts(posts: List<Post>) {
		this.posts.apply {
			clear()
			addAll(posts)
		}
	}
}