package ai.lingopulse.util

import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.KeychainSettings
import com.russhwolf.settings.Settings

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
@OptIn(ExperimentalSettingsImplementation::class)
actual class Storage actual constructor(val name: String) {
    actual fun get(): Settings = KeychainSettings.Factory().create(name)
}