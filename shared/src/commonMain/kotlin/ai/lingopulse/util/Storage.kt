package ai.lingopulse.util

import com.russhwolf.settings.Settings

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class Storage(name: String){
    fun get(): Settings
}