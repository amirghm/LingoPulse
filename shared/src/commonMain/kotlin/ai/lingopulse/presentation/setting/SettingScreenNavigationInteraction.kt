package ai.lingopulse.presentation.setting

sealed class SettingScreenNavigationInteraction {
    data object NavigateToFeeds : SettingScreenNavigationInteraction()
}