package com.helpquest.core.presentation.pagination

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun BannerListener(
    lazyListState: LazyListState,
    elements: List<Any>,
    isBannerVisible: Boolean,
    onShowBanner: (topVisibleItemIndex: Int) -> Unit,
    onHide: () -> Unit
) {
    val isBannerVisibleUpdated by rememberUpdatedState(isBannerVisible)



    LaunchedEffect(lazyListState, elements) {
        snapshotFlow {
            val info = lazyListState.layoutInfo
            val visibleItems = info.visibleItemsInfo
            val total = info.totalItemsCount

            val oldestVisibleElementIndex = visibleItems.maxOfOrNull { it.index } ?: -1

            val isAtOldestElements = oldestVisibleElementIndex >= total - 1
            val isAtNewestElements = visibleItems.any { it.index == 0 }
            ElementBannerScrollState(
                oldestVisibleElementIndex = oldestVisibleElementIndex,
                isScrollInProgress = lazyListState.isScrollInProgress,
                isAtEdgeOfList = isAtOldestElements || isAtNewestElements
            )
        }
            .distinctUntilChanged()
            .collect { (oldestVisibleIndex, isScrollInProgress, isAtEdgeOfList) ->
                val shouldShowBanner = isScrollInProgress &&
                        !isAtEdgeOfList &&
                        oldestVisibleIndex >= 0

                when {
                    shouldShowBanner -> onShowBanner(oldestVisibleIndex)
                    !shouldShowBanner && isBannerVisibleUpdated -> {
                        delay(1000L)
                        onHide()
                    }
                }
            }
    }
}

data class ElementBannerScrollState(
    val oldestVisibleElementIndex: Int,
    val isScrollInProgress: Boolean,
    val isAtEdgeOfList: Boolean
)