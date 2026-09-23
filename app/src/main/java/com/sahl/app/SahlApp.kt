// SPDX-License-Identifier: AGPL-3.0-or-later
// Copyright (C) 2026 Syeed Syed Asif

package com.sahl.app

import androidx.activity.compose.BackHandler
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.sahl.app.navigation.TopLevelDestination
import com.sahl.app.ui.learn.LearnScreen
import com.sahl.app.ui.profile.ProfileScreen
import com.sahl.app.ui.quran.QuranScreen
import com.sahl.app.ui.theme.SahlTheme

/**
 * The app's root: the main navigation and the selected tab's screen.
 *
 * The navigation adapts to the window: a bottom bar when the window is narrow, a side rail
 * when it is wide.
 */
@Composable
fun SahlApp() {
    // Saveable, so the selected tab survives rotation and the process being killed.
    var current by rememberSaveable { mutableStateOf(TopLevelDestination.START) }

    // Back from another tab returns to the start tab before leaving the app.
    BackHandler(enabled = current != TopLevelDestination.START) {
        current = TopLevelDestination.START
    }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            TopLevelDestination.entries.forEach { destination ->
                val selected = destination == current
                item(
                    selected = selected,
                    onClick = { current = destination },
                    icon = {
                        Icon(
                            painter = painterResource(
                                if (selected) destination.selectedIcon else destination.icon,
                            ),
                            // The label already names the tab for screen readers.
                            contentDescription = null,
                        )
                    },
                    label = { Text(stringResource(destination.label)) },
                )
            }
        },
    ) {
        when (current) {
            TopLevelDestination.LEARN -> LearnScreen()
            TopLevelDestination.QURAN -> QuranScreen()
            TopLevelDestination.PROFILE -> ProfileScreen()
        }
    }
}

@Preview(name = "Phone", device = "spec:width=411dp,height=891dp")
@Preview(name = "Tablet", device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun SahlAppPreview() {
    SahlTheme {
        SahlApp()
    }
}
