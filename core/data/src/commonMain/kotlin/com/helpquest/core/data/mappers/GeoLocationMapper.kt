package com.helpquest.core.data.mappers

import com.helpquest.core.data.dto.GeoLocationDto
import com.helpquest.core.domain.models.GeoLocation

fun GeoLocationDto.toGeoLocation() = GeoLocation(latitude, longitude)

fun GeoLocation.toGeoLocationDto() = GeoLocationDto(latitude, longitude)