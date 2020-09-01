package ru.bmstu.iu9.vrsocialnetwork.ui.home

import android.util.Log
import android.view.View
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.post.view.*
import kotlinx.coroutines.tasks.await
import ru.bmstu.iu9.vrsocialnetwork.R
import ru.bmstu.iu9.vrsocialnetwork.data.model.Post
import java.io.File

class FeedHolder (itemView: View): RecyclerView.ViewHolder(itemView) {
	var mPostView: CardView = itemView.findViewById(R.id.post_card)
	var mDate: Long = 0
	val mStorage = FirebaseStorage.getInstance()

	init {
		mPostView.setOnClickListener { v -> }
	}

	fun bind(post: Post) {
		with(post) {

			mPostView.post_profile_image.load(mPostView.context.getDrawable(R.drawable.baseline_face_black_18dp))

			mPostView.post_nickname.text = authorName
			Log.d(TAG, imageLink)
			mStorage.reference.child(imageLink).downloadUrl
				.addOnSuccessListener {
					mPostView.post_image.load(it)
				}
//			mPostView.post_image.load(File(imageLink))
		}
	}

	companion object {
		private const val TAG = "FEED HOLDER"
	}
}