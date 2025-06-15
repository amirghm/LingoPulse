package ai.lingopulse.util

import android.content.Context
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class Storage actual constructor(val name: String) : KoinComponent {
    private val context: Context by inject()
    actual fun get(): Settings = SharedPreferencesSettings.Factory(context = context)
        .create(name)
}
