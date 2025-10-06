@file:OptIn(ExperimentalTestApi::class)

package com.helpquest

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test

class ExampleUiTest {

    @Test
    fun uiTestExample() = runComposeUiTest {
        setContent {
            //specific Ui component
        }

        //perform actions and assertions
    }
}