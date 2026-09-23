// SPDX-License-Identifier: AGPL-3.0-or-later
// Copyright (C) 2026 Syeed Syed Asif

package com.sahl.app.ui.learn

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sahl.app.R
import com.sahl.app.ui.components.PlaceholderScreen

@Composable
fun LearnScreen(modifier: Modifier = Modifier) {
    PlaceholderScreen(
        title = R.string.nav_learn,
        message = R.string.learn_placeholder,
        modifier = modifier,
    )
}
