package ru.bmstu.iu9.vrsocialnetwork.ui.preview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.bmstu.iu9.vrsocialnetwork.R

class PostPreviewFragment: Fragment() {

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		val root = inflater.inflate(R.layout.fragment_post_preview, container, false)
		return root
	}
}