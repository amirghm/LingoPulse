package ai.lingopulse.util.extension

import androidx.compose.runtime.Composable
import ai.lingopulse.shared.Platform
import ai.lingopulse.shared.isAndroid
import ai.lingopulse.shared.isDesktop
import ai.lingopulse.shared.isWeb
import ai.lingopulse.shared.isIOS
import org.koin.compose.getKoin

@Composable
fun isAndroid() = getKoin().get<Platform>().isAndroid()
@Composable
fun isIOS() = getKoin().get<Platform>().isIOS()
@Composable
fun isDesktop() = getKoin().get<Platform>().isDesktop()
@Composable
fun isWeb() = getKoin().get<Platform>().isWeb()
