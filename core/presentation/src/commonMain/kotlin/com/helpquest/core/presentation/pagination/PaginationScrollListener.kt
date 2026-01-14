package com.helpquest.core.presentation.pagination

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun PaginationScrollListener(
    lazyListState: LazyListState,
    itemCount: Int,
    isPaginationLoading: Boolean,
    isEndReached: Boolean,
    onNearEnd: () -> Unit
) {
    val updatedItemCount by rememberUpdatedState(itemCount)
    val isPaginationLoading by rememberUpdatedState(isPaginationLoading)
    val isEndReached by rememberUpdatedState(isEndReached)

    var lastTriggerItemCount by remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(lazyListState) {
        snapshotFlow {
            val info = lazyListState.layoutInfo
            val total = info.totalItemsCount
            val endVisibleIndex = info.visibleItemsInfo.lastOrNull()?.index
            val remainingItems = if (endVisibleIndex != null) {
                total - endVisibleIndex - 1
            } else null

            PaginationScrollState(
                currentItemCount = updatedItemCount,
                isEligible = remainingItems != null &&
                        remainingItems <= 5 &&
                        !isPaginationLoading &&
                        !isEndReached
            )
        }
            .distinctUntilChanged()
            .collect { (itemCount, isEligible) ->
                val shouldTrigger = isEligible && itemCount > lastTriggerItemCount

                if (shouldTrigger) {
                    lastTriggerItemCount = itemCount
                    onNearEnd()
                }
            }
    }
}

data class PaginationScrollState(
    val currentItemCount: Int,
    val isEligible: Boolean
)