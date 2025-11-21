package com.helpquest.core.domain.models

enum class SubClass(
    val subClassId: String,
    val subClassName: String,
    val classParent: Class,
    val isProfessional: Boolean = false,
) {
    SOFTWARE_MAGE(
        subClassId = "1001",
        subClassName = "Software Mage",
        classParent = Class.TECH_WIZARD,
        isProfessional = true
    ),
    HARDWARE_MAGE(
        subClassId = "1002",
        subClassName = "Hardware Mage",
        classParent = Class.TECH_WIZARD,
        isProfessional = true
    ),
    GEEK_MAGE(
        subClassId = "1003",
        subClassName = "Geek",
        classParent = Class.TECH_WIZARD,
        isProfessional = false
    )
}
