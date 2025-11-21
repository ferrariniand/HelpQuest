package com.helpquest.core.domain.models

enum class Class(
    val classId: String,
    val className: String,
    val classCategory: Category,
    val classImageUrl: String? = null,
    val subClassList: List<SubClass> = emptyList()
) {
    VILLAGER(
        classId = "0000",
        className = "Villager",
        classCategory = Category.GENERIC,
        classImageUrl = "test"
    ),
    TECH_WIZARD(
        classId = "1000",
        className = "Tech Wizard",
        classCategory = Category.TECHNOLOGY,
        subClassList = listOf(SubClass.SOFTWARE_MAGE, SubClass.HARDWARE_MAGE, SubClass.GEEK_MAGE)
    )
}
