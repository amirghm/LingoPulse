package ai.lingopulse.shared

import kotlinx.browser.window

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class Platform {
    actual val id: String = PLATFORM_WEB
    actual val name: String = "Web with Kotlin/Wasm"

    actual fun copyToClipboard(text: String) {
        window.navigator.clipboard.writeText(text)
        window.navigator.vibrate(50)
    }
}
