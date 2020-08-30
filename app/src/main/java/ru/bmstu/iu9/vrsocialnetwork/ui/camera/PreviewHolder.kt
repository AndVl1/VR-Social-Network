package ru.bmstu.iu9.vrsocialnetwork.ui.camera

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import ru.bmstu.iu9.vrsocialnetwork.R
import java.io.File

class PreviewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
	private val mImageView = itemView.findViewById<ImageView>(R.id.preview_image)

	fun bind(path: String) {
		mImageView.load(File(path))
	}
}