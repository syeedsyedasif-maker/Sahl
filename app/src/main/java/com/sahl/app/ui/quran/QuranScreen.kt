// SPDX-License-Identifier: AGPL-3.0-or-later
// Copyright (C) 2026 Syeed Syed Asif

package com.sahl.app.ui.quran

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sahl.app.R
import com.sahl.app.ui.components.PlaceholderScreen

@Composable
fun QuranScreen(modifier: Modifier = Modifier) {
    PlaceholderScreen(
        title = R.string.nav_quran,
        message = R.string.quran_placeholder,
        modifier = modifier,
    )
}
