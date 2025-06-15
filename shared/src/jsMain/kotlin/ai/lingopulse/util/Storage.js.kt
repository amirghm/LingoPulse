package ai.lingopulse.util

import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.Settings
import kotlinx.browser.localStorage

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
@OptIn(ExperimentalSettingsImplementation::class)
actual class Storage actual constructor(val name: String) {
    actual fun get(): Settings = StorageSettings(name = name, delegate = localStorage)
}