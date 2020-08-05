package ru.bmstu.iu9.vrsocialnetwork.ui.camera

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.bmstu.iu9.vrsocialnetwork.R
import java.io.File

class PreviewAdapter(private val parentFile: File): RecyclerView.Adapter<PreviewHolder>() {
	private val files = parentFile.list()

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviewHolder =
		PreviewHolder(LayoutInflater.from(parent.context).inflate(R.layout.preview_image, parent, false))


	override fun getItemCount(): Int = files.size

	override fun onBindViewHolder(holder: PreviewHolder, position: Int) {
		holder.bind("${parentFile.absolutePath}/${files[position]}")
		Log.d(TAG, "${parentFile.absolutePath}/${files[position]}")
	}

	companion object {
		private const val TAG = "PREVIEW ADAPTER"
	}
}

