package ru.bmstu.iu9.vrsocialnetwork.ui.nav

import ru.bmstu.iu9.vrsocialnetwork.R

object NavigationModel {
	const val PROFILE_ID = 0
	const val FAVORITE_ID = 1
	const val SETTINGS_ID = 2

	private var navMenuItems = mutableListOf(
		NavigationModelItem.NavMenuItem(
			id = PROFILE_ID,
			icon = R.drawable.ic_profile_black_24dp,
			titleRes = R.string.title_profile
		),
		NavigationModelItem.NavMenuItem(
			id = FAVORITE_ID,
			icon = R.drawable.outline_favorite_border_24dp,
			titleRes = R.string.favorite
		),
		NavigationModelItem.NavMenuItem(
			id = SETTINGS_ID,
			icon = R.drawable.outline_settings_24dp,
			titleRes = R.string.settings
		)
	)
}