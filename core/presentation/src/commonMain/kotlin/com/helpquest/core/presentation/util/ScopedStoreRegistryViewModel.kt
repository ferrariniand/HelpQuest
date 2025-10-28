package com.helpquest.core.presentation.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore

/** ViewModel for registering and clearing scoped stores:
 *     used to avoid that Dialog/BottomSheet ViewModel(and parent information) is destroyed during recomposition of a Composable
 **/
class ScopedStoreRegistryViewModel : ViewModel() {

    private val stores = mutableMapOf<String, ViewModelStore>()

    fun getOrCreate(id: String): ViewModelStore =
        stores.getOrPut(id) { ViewModelStore() }

    fun clear(id: String) {
        stores.remove(id)?.clear()
    }

    override fun onCleared() {
        super.onCleared()
        stores.values.forEach { it.clear() }
        stores.clear()
    }
}