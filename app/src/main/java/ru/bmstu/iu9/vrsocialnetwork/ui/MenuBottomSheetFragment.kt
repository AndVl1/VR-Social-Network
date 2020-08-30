package ru.bmstu.iu9.vrsocialnetwork.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import com.github.heyalex.bottomdrawer.BottomDrawerDialog
import com.github.heyalex.bottomdrawer.BottomDrawerFragment
import com.github.heyalex.handle.PlainHandleView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputLayout
import ru.bmstu.iu9.vrsocialnetwork.R


class MenuBottomSheetFragment : BottomDrawerFragment() {

	private var mAlphaCancelButton = 0f

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		val mRoot = inflater.inflate(R.layout.fragment_bottom_appbar_menu, container, false)
		val percent = 0.65f

		addBottomSheetCallback {
			onSlide { _, slideOffset ->
				val alphaTemp = (slideOffset - percent) * (1f / (1f - percent))
				mAlphaCancelButton = if (alphaTemp >= 0) {
					alphaTemp
				} else {
					0f
				}
			}
		}

		return mRoot
	}

	override fun configureBottomDrawer(): BottomDrawerDialog {
		return BottomDrawerDialog.build(requireContext()) {
			theme = R.style.Plain
			handleView = PlainHandleView(context).apply {
				val widthHandle =
					resources.getDimensionPixelSize(R.dimen.bottom_sheet_handle_width)
				val heightHandle =
					resources.getDimensionPixelSize(R.dimen.bottom_sheet_handle_height)
				val params =
					FrameLayout.LayoutParams(widthHandle, heightHandle, Gravity.CENTER_HORIZONTAL)

				params.topMargin =
					resources.getDimensionPixelSize(R.dimen.bottom_sheet_handle_top_margin)

				layoutParams = params
			}
		}
	}

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		outState.putFloat("alphaCancelButton", mAlphaCancelButton)
	}

	override fun onViewStateRestored(savedInstanceState: Bundle?) {
		super.onViewStateRestored(savedInstanceState)
		mAlphaCancelButton = savedInstanceState?.getFloat("alphaCancelButton") ?: 0f
	}

	companion object {
		const val EMPTY_TEXT = ""
	}
}