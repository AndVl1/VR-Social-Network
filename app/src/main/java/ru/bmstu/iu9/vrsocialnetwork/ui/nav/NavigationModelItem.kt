package ru.bmstu.iu9.vrsocialnetwork.ui.nav

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

sealed class NavigationModelItem {

	data class NavMenuItem(
		val id: Int,
		@DrawableRes val icon: Int,
		@StringRes val titleRes: Int
	): NavigationModelItem()
}