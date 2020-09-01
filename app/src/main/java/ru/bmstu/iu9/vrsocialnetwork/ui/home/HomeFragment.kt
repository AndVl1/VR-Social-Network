package ru.bmstu.iu9.vrsocialnetwork.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ru.bmstu.iu9.vrsocialnetwork.R
import ru.bmstu.iu9.vrsocialnetwork.data.model.Post
import ru.bmstu.iu9.vrsocialnetwork.utils.Status

@AndroidEntryPoint
class HomeFragment : Fragment() {

	private var mRecyclerView: RecyclerView? = null
	private var mLinearLayoutManager : LinearLayoutManager? = null
	private var mRoot: View? = null
	private lateinit var mAdapter: FeedAdapter

	private val homeViewModel by viewModels<HomeViewModel>()

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
		setupObservers()

		return mRoot
	}

	private fun setupObservers() {
		homeViewModel.downloadPosts().observe(viewLifecycleOwner, {
			it?.let {resource ->
				when (resource.status) {
					Status.SUCCESS -> {
						mRecyclerView?.visibility = View.VISIBLE
						resource.data?.let { users -> retrieveList(users)}
					}
					Status.LOADING -> {

					}
					Status.ERROR -> {
						Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT)
							.show()
					}
				}
			}
		})
	}

	private fun retrieveList(posts: List<Post>) {
		mAdapter.apply {
			addPosts(posts)
			notifyDataSetChanged()
		}
	}

	private fun initializeList() {
		mRecyclerView = mRoot?.findViewById(R.id.recyclerContainer)
		mLinearLayoutManager = LinearLayoutManager(this.context)
		mAdapter = FeedAdapter(arrayListOf())
		mRecyclerView?.layoutManager = mLinearLayoutManager
		mRecyclerView?.adapter = mAdapter
//		val adapter = FeedPagedAdapter()
//		homeViewModel.mPostsList.observe(viewLifecycleOwner, Observer {
//			adapter.submitList(it)
//		})
	}

	companion object {
		const val TAG = "HOME FRAGMENT"
	}
}
