package ai.lingopulse.shared

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class Platform {
    val id: String
    val name: String
    fun copyToClipboard(text: String)
}

fun Platform.isAndroid() = id == PLATFORM_ANDROID
fun Platform.isIOS() = id == PLATFORM_IOS
fun Platform.isDesktop() = id == PLATFORM_DESKTOP
fun Platform.isWeb() = id == PLATFORM_WEB