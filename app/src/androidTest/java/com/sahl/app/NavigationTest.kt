// SPDX-License-Identifier: AGPL-3.0-or-later
// Copyright (C) 2026 Syeed Syed Asif

package com.sahl.app

import androidx.annotation.StringRes
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.espresso.Espresso
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** Instrumented tests for the main navigation. Run on a device or emulator. */
@RunWith(AndroidJUnit4::class)
class NavigationTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private fun string(@StringRes id: Int) = composeTestRule.activity.getString(id)

    // Tabs are selectable; each screen's title repeats the tab label but is not.
    private fun tab(@StringRes label: Int) =
        composeTestRule.onNode(hasText(string(label)) and isSelectable())

    @Test
    fun opensOnLearn() {
        tab(R.string.nav_learn).assertIsSelected()
        composeTestRule.onNodeWithText(string(R.string.learn_placeholder)).assertIsDisplayed()
    }

    @Test
    fun tappingATabShowsItsScreen() {
        tab(R.string.nav_quran).performClick()

        tab(R.string.nav_quran).assertIsSelected()
        composeTestRule.onNodeWithText(string(R.string.quran_placeholder)).assertIsDisplayed()
    }

    @Test
    fun backReturnsToLearnBeforeLeaving() {
        tab(R.string.nav_profile).performClick()

        Espresso.pressBack()

        tab(R.string.nav_learn).assertIsSelected()
        composeTestRule.onNodeWithText(string(R.string.learn_placeholder)).assertIsDisplayed()
    }

    @Test
    fun selectedTabSurvivesRecreation() {
        tab(R.string.nav_quran).performClick()

        // Same path as rotation or a language change: the Activity is destroyed and rebuilt.
        composeTestRule.activityRule.scenario.recreate()

        tab(R.string.nav_quran).assertIsSelected()
    }
}
