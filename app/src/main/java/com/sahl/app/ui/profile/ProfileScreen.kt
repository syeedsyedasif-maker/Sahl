// SPDX-License-Identifier: AGPL-3.0-or-later
// Copyright (C) 2026 Syeed Syed Asif

package com.sahl.app.ui.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sahl.app.R
import com.sahl.app.ui.components.PlaceholderScreen

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    PlaceholderScreen(
        title = R.string.nav_profile,
        message = R.string.profile_placeholder,
        modifier = modifier,
    )
}
