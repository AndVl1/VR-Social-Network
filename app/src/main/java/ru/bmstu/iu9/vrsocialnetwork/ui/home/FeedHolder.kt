package ru.bmstu.iu9.vrsocialnetwork.ui.home

import android.view.View
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import kotlinx.android.synthetic.main.post.view.*
import ru.bmstu.iu9.vrsocialnetwork.R
import ru.bmstu.iu9.vrsocialnetwork.data.model.Post

class FeedHolder (itemView: View): RecyclerView.ViewHolder(itemView) {
	var mPostView: CardView = itemView.findViewById(R.id.post_card)
	var mDate: Long = 0

	init {
		mPostView.setOnClickListener { v -> }
	}

	fun bind(post: Post) {
		with(post) {
			if (imageLink != null) {
				mPostView.post_profile_image.load(imageLink)
				mPostView.post_profile_image.visibility = View.VISIBLE
			} else {
				mPostView.post_profile_image.visibility = View.INVISIBLE
			}
			mPostView.post_nickname.text = authorName
		}
	}
}