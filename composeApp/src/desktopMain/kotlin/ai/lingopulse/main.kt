package ai.lingopulse

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ai.lingopulse.di.initKoin
import ai.lingopulse.sandbox.startSandboxServer
import ai.lingopulse.shared.APP_ID
import ai.lingopulse.shared.APP_NAME
import ai.lingopulse.shared.jbrVersion
import ai.lingopulse.shared.kcefCachePath
import ai.lingopulse.shared.kcefPath
import ai.lingopulse.shared.macTitleAppNameKey
import com.multiplatform.webview.util.addTempDirectoryRemovalHook
import dev.datlag.kcef.KCEF
import io.github.vinceglb.filekit.FileKit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.stringResource
import lingopulse.composeapp.generated.resources.Res
import lingopulse.composeapp.generated.resources.desktop__initializing
import lingopulse.composeapp.generated.resources.desktop__restart_required
import lingopulse.composeapp.generated.resources.desktop__restart_required_message
import java.io.File

fun main() {
    initKoin()
    FileKit.init(appId = APP_ID)
    System.setProperty(macTitleAppNameKey, APP_NAME);
    startSandboxServer()
    application {
        addTempDirectoryRemovalHook()
        Window(onCloseRequest = ::exitApplication, title = APP_NAME) {
            var restartRequired by remember { mutableStateOf(false) }
            var downloading by remember { mutableStateOf(0F) }
            var initialized by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                withContext(Dispatchers.IO) {
                    KCEF.init(builder = {
                        installDir(File(kcefPath()))
                        progress {
                            onDownloading {
                                downloading = it
                            }
                            onInitialized {
                                initialized = true
                            }
                        }
                        download {
                            github {
                                release(jbrVersion)
                            }
                        }

                        settings {
                            cachePath = File(kcefCachePath()).absolutePath
                        }
                    }, onError = {
                        it?.printStackTrace()
                    }, onRestartRequired = {
                        restartRequired = true
                    })
                }
            }

            MaterialTheme {
                if (restartRequired) {
                    RestartRequiredScreen()
                } else {
                    if (initialized) {
                        LingoPulseApp()
                    } else {
                        LoadingScreen(downloadProgress = downloading)
                    }
                }
            }

            DisposableEffect(Unit) {
                onDispose {
                    KCEF.disposeBlocking()
                }
            }
        }
    }
}

@Composable
fun LoadingScreen(downloadProgress: Float) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(32.dp),
                color = Color.Black,
                strokeWidth = 2.dp,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = APP_NAME,
                fontSize = 18.sp,
                color = Color.Black,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(Res.string.desktop__initializing),
                fontSize = 12.sp,
                color = Color.Black.copy(alpha = 0.7f),
            )

            Spacer(modifier = Modifier.height(16.dp))

            LinearProgressIndicator(
                progress = downloadProgress / 100f,
                modifier =
                    Modifier
                        .size(
                            width = 200.dp,
                            height = 4.dp,
                        ),
                color = Color.Black,
                backgroundColor = Color.Black.copy(alpha = 0.2f),
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${downloadProgress.toInt()}%",
                fontSize = 12.sp,
                color = Color.Black,
            )
        }
    }
}

@Composable
fun RestartRequiredScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(Res.string.desktop__restart_required),
                fontSize = 18.sp,
                color = Color.Black,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(Res.string.desktop__restart_required_message),
                fontSize = 12.sp,
                color = Color.Black.copy(alpha = 0.7f),
            )
        }
    }
}