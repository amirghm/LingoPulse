package ai.lingopulse.ui.feature.setting

import ai.lingopulse.presentation.common.model.UiCategory
import ai.lingopulse.presentation.setting.model.UiLanguageLevel
import ai.lingopulse.presentation.setting.model.UiLanguage
import ai.lingopulse.ui.core.common.model.ScreenEvent

sealed class SettingScreenEvent: ScreenEvent() {
    data class OnCategoryClicked(val category: UiCategory) : SettingScreenEvent()
    data class OnLanguageLevelClicked(val languageLevel: UiLanguageLevel) : SettingScreenEvent()
    data class OnLanguageClicked(val language: UiLanguage) : SettingScreenEvent()
    object OnConfirmClicked : SettingScreenEvent()
}