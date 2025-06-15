package ai.lingopulse

import androidx.compose.ui.window.ComposeUIViewController
import ai.lingopulse.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }, content = {
        LingoPulseApp()
    }
)