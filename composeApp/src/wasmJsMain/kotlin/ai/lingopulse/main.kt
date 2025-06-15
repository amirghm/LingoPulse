package ai.lingopulse

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import ai.lingopulse.di.initKoin
import kotlinx.browser.document
import kotlinx.browser.window

const val SERVER_PORT = 8080

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initKoin()
    ComposeViewport(document.body!!) {
        LingoPulseApp()
    }
    window.onunload = {
        // localStorage.clear()
    }
}