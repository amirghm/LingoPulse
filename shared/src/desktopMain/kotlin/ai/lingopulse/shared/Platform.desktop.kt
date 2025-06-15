package ai.lingopulse.shared

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.exists
import io.github.vinceglb.filekit.readString
import io.github.vinceglb.filekit.writeString
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class Platform {
    actual val id: String = PLATFORM_DESKTOP
    actual val name: String = "Java ${System.getProperty("java.version")}"

    actual fun copyToClipboard(text: String) {
        val clipboard = Toolkit.getDefaultToolkit().systemClipboard
        val selection = StringSelection(text)
        clipboard.setContents(selection, null)
        println("Copied to clipboard: $text")

        Toolkit.getDefaultToolkit().beep()
    }
}
