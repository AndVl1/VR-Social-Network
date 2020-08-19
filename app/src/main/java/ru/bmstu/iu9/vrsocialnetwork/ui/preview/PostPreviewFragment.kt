package ru.bmstu.iu9.vrsocialnetwork.ui.preview

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.bmstu.iu9.vrsocialnetwork.R
import ru.bmstu.iu9.vrsocialnetwork.ui.camera.PreviewAdapter
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
	): View? = inflater.inflate(R.layout.fragment_post_preview, container, false)

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		val folder = File(mNavArgs.filePath)
		folder.list()?.forEach {
			Log.d(TAG, it)
		}
		initRecycler(view, folder)
		view.findViewById<Button>(R.id.preview_submitButton)
			.setOnClickListener {
				view.findViewById<ProgressBar>(R.id.uploading_progress)
					.visibility = View.VISIBLE

				viewLifecycleOwner.lifecycleScope.launch {
					mViewModel.addModel(mNavArgs.filePath, mNavArgs.sensorMap)
					mViewModel.mCompleteLiveData.observe(viewLifecycleOwner, {
						if (it == true) {
							navigateHome()
						} else if (it == false) {
							Toast.makeText(
								requireContext(),
								getString(R.string.error_storage),
								Toast.LENGTH_SHORT
							).show()
						}
						Log.d(TAG, it.toString())
					})
				}
			}
		view.findViewById<Button>(R.id.preview_retryButton)
			.setOnClickListener {
				File(mNavArgs.filePath).delete()
				requireActivity().onBackPressed()
			}
	}

	private fun initRecycler(view: View, parentFile: File) {
		val recyclerView = view.findViewById<RecyclerView>(R.id.content_preview)
		val gridLayoutManager = GridLayoutManager(requireContext(), COLUMNS)
		recyclerView.layoutManager = gridLayoutManager
		val adapter = PreviewAdapter(parentFile)
		recyclerView.adapter = adapter
	}

	private fun navigateHome() {
		val direction = PostPreviewFragmentDirections.actionPostPreviewFragmentToHomeFragment()
		findNavController().navigate(direction)
	}

	companion object {
		private const val TAG = "POST PREVIEW"
		private const val COLUMNS = 4
	}
}