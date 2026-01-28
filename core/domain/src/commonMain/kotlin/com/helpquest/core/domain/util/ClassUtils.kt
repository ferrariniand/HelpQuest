package com.helpquest.core.domain.util

import com.helpquest.core.domain.models.Class
import com.helpquest.core.domain.models.SubClass

object ClassUtils {
    fun findClassById(classId: String): Class =
        Class.entries.find { it.classId == classId } ?: Class.VILLAGER

    fun findSubClassById(subClassId: String?): SubClass? =
        SubClass.entries.find { it.subClassId == subClassId }
}