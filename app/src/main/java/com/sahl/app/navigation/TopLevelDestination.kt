// SPDX-License-Identifier: AGPL-3.0-or-later
// Copyright (C) 2026 Syeed Syed Asif

package com.sahl.app.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.sahl.app.R

/** The tabs in the main navigation, in display order. */
enum class TopLevelDestination(
    @StringRes val label: Int,
    @DrawableRes val icon: Int,
    @DrawableRes val selectedIcon: Int,
) {
    LEARN(R.string.nav_learn, R.drawable.ic_school, R.drawable.ic_school_filled),
    QURAN(R.string.nav_quran, R.drawable.ic_menu_book, R.drawable.ic_menu_book_filled),
    PROFILE(R.string.nav_profile, R.drawable.ic_person, R.drawable.ic_person_filled),
    ;

    companion object {
        /** Where the app opens, and where Back returns to from the other tabs. */
        val START = LEARN
    }
}
