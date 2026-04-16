package com.helpquest.core.data.preferences


import com.helpquest.core.domain.preferences.ThemePreference
import com.helpquest.core.domain.preferences.ThemePreferences
import eu.anifantakis.lib.ksafe.KSafe
import kotlinx.coroutines.flow.Flow

class KSafeThemePreferences(
    private val ksafe: KSafe
) : ThemePreferences {

    private val themePreferenceKey = "theme_preference"

    override fun observeThemePreference(): Flow<ThemePreference> {
        return ksafe.getFlow(
            key = themePreferenceKey,
            defaultValue = ThemePreference.SYSTEM
        )
    }

    override suspend fun updateThemePreference(theme: ThemePreference) {
        ksafe.put(
            key = themePreferenceKey,
            value = theme
        )
    }
}