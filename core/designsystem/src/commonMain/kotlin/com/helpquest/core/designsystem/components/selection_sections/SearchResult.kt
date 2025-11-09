package com.helpquest.core.designsystem.components.selection_sections

sealed class SearchResult<out T> {
    data class Success<T>(val data: T) : SearchResult<T>()
    data object NotFound : SearchResult<Nothing>()

    fun getSearchResultOrNull(): T? = (this as? Success)?.data

    fun isSuccess(): Boolean = this is Success
    fun isNotFound(): Boolean = this is NotFound

}