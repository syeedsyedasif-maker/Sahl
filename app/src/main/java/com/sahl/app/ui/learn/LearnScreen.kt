// SPDX-License-Identifier: AGPL-3.0-or-later
// Copyright (C) 2026 Syeed Syed Asif

package com.sahl.app.ui.learn

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.sahl.app.R
import com.sahl.app.ui.components.PlaceholderScreen

@Composable
fun LearnScreen(modifier: Modifier = Modifier) {
    PlaceholderScreen(
        title = R.string.nav_learn,
        message = R.string.learn_placeholder,
        modifier = modifier,
    ) {
        Text(
            text = stringResource(R.string.release_check),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}
