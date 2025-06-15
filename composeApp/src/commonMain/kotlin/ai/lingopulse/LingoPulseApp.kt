package ai.lingopulse

import ai.lingopulse.ui.LingoPulseNavigation
import ai.lingopulse.ui.core.theme.LingoPulseTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.kodein.emoji.compose.EmojiService

@Composable
@Preview
fun LingoPulseApp() {
    remember { EmojiService.initialize() }
    LingoPulseTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background,
        ) {
            LingoPulseNavigation()
        }
    }
}
