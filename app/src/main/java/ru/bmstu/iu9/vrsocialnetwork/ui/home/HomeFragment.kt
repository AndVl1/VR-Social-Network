package ru.bmstu.iu9.vrsocialnetwork.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ru.bmstu.iu9.vrsocialnetwork.R
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

	private var mRecyclerView: RecyclerView? = null
	private var mLinearLayoutManager : LinearLayoutManager? = null
	private var mRoot: View? = null

	private val homeViewModel: HomeViewModel by viewModels()
	private val postsAdapter = FeedPagedAdapter()

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		Log.d(TAG, "onCreateView")
		if (mRoot == null) {
			mRoot = inflater.inflate(R.layout.fragment_home, container, false)
		}

		initializeList()
//		observeLiveData()

		return mRoot
	}

	private fun observeLiveData() {
		homeViewModel.getPosts().observe(viewLifecycleOwner, Observer {
			postsAdapter.submitList(it)
		})
	}

	private fun initializeList() {
		mRecyclerView = mRoot?.findViewById(R.id.recyclerContainer)
		mLinearLayoutManager = LinearLayoutManager(this.context)
		val adapter = FeedPagedAdapter()
		mRecyclerView?.layoutManager = mLinearLayoutManager
		mRecyclerView?.adapter = adapter
	}

	companion object {
		const val TAG = "HOME FRAGMENT"
	}
}