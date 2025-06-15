package ai.lingopulse.shared

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.readString
import io.github.vinceglb.filekit.toKotlinxIoPath
import io.github.vinceglb.filekit.writeString
import kotlinx.io.files.SystemFileSystem
import platform.AudioToolbox.AudioServicesPlaySystemSound
import platform.UIKit.UIDevice
import platform.UIKit.UIPasteboard

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class Platform {
    actual val id: String = PLATFORM_IOS
    actual val name: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion

    actual fun copyToClipboard(text: String) {
        UIPasteboard.generalPasteboard.string = text
        AudioServicesPlaySystemSound(0x00000FFFu) // kSystemSoundID_Vibrate
    }
}
