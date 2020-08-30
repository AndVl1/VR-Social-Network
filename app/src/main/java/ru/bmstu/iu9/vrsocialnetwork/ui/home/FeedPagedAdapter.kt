package ru.bmstu.iu9.vrsocialnetwork.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import ru.bmstu.iu9.vrsocialnetwork.R
import ru.bmstu.iu9.vrsocialnetwork.data.model.Post

class FeedPagedAdapter: PagedListAdapter<Post, FeedHolder>(DIFF_CALLBACK) {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedHolder = FeedHolder(
		LayoutInflater.from(parent.context).inflate(R.layout.post, parent, false)
	)

	override fun onBindViewHolder(holder: FeedHolder, position: Int) {
		holder.bind(getItem(position)!!)
	}

	companion object{
		private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Post>() {
			override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean =
				oldItem.id == newItem.id

			override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean =
				oldItem == newItem

		}

	}
}