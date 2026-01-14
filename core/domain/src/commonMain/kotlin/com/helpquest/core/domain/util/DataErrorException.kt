package com.helpquest.core.domain.util

class DataErrorException(
    val error: DataError
) : Exception()