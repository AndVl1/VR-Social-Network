package ru.bmstu.iu9.vrsocialnetwork.ui.preview

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import coil.api.load
import dagger.hilt.android.AndroidEntryPoint
import ru.bmstu.iu9.vrsocialnetwork.R
import ru.bmstu.iu9.vrsocialnetwork.ui.home.HomeFragmentDirections
import java.io.File

@AndroidEntryPoint
class PostPreviewFragment: Fragment() {

	private val mNavArgs: PostPreviewFragmentArgs by navArgs()
	private val mViewModel by viewModels<PostPreviewViewModel>()

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.fragment_post_preview, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		val imagePreview = view.findViewById<ImageView>(R.id.content_preview)
		imagePreview.load(File(mNavArgs.filePath))
		view.findViewById<Button>(R.id.preview_submitButton)
			.setOnClickListener {
				mViewModel.savePost(mNavArgs.filePath)
				// TODO navigate home after successful uploading
				navigateHome()
			}
		view.findViewById<Button>(R.id.preview_retryButton)
			.setOnClickListener {
				File(mNavArgs.filePath).delete()
				requireActivity().onBackPressed()
			}
	}

	private fun navigateHome() {
		val direction = PostPreviewFragmentDirections.actionPostPreviewFragmentToHomeFragment()
		findNavController().navigate(direction)
	}

	companion object {
		const val TAG = "POST PREVIEW"
	}
}