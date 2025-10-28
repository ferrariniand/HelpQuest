@file:OptIn(ExperimentalUuidApi::class)

package com.helpquest.core.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import org.koin.compose.viewmodel.koinViewModel
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

//TODO: Could have hidden issues and edge cases in witch the ViewModel is destroyed
@Composable
fun DialogSheetScopedViewModelContainer(
    visible: Boolean,
    scopeId: String = rememberSaveable {
        Uuid.random().toString()
    }, //unique id for each Dialog/BottomSheet
    content: @Composable () -> Unit
) {
    val parentOwner = LocalViewModelStoreOwner.current
        ?: throw IllegalStateException("No parent owner found")

    //information of parent owner are stored in a ViewModel to avoid the loss of them during recomposition
    val registry = koinViewModel<ScopedStoreRegistryViewModel>(
        viewModelStoreOwner = parentOwner
    )

    var owner by remember { mutableStateOf<ViewModelStoreOwner?>(null) }

    LaunchedEffect(visible, scopeId) {
        if (visible && owner == null) {
            //register parent owner in the ViewModel
            owner = object : ViewModelStoreOwner {
                override val viewModelStore: ViewModelStore
                    get() = registry.getOrCreate(scopeId)
            }
        } else if (!visible && owner != null) {
            //if Dialog/BottomSheet is closed and parent owner is still registered, clear the ViewModel
            registry.clear(scopeId)
            owner = null
        }
    }

    owner?.let { dialogOwner ->
        //if parent owner exists, provide it to the content
        CompositionLocalProvider(LocalViewModelStoreOwner provides dialogOwner) {
            content()
        }
    }
}