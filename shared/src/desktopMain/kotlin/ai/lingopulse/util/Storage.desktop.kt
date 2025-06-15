package ai.lingopulse.util

import ai.lingopulse.shared.APP_NAME
import com.russhwolf.settings.PreferencesSettings
import com.russhwolf.settings.Settings

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class Storage actual constructor(val name: String) {
    actual fun get(): Settings = PreferencesSettings.Factory().create("$APP_NAME/$name")
}