package ai.lingopulse.presentation.common.mapper

import ai.lingopulse.domain.common.model.LanguageLevel
import ai.lingopulse.presentation.setting.model.UiLanguageLevel

fun LanguageLevel.toUiLanguageLevel(selected: Boolean) = UiLanguageLevel(
    id = this.id,
    level = this.level,
    isSelected = selected
)

fun List<LanguageLevel>.toUiLanguageLevels(selectedLanguageLevel: String = "") =
    this.map { it.toUiLanguageLevel(it.id == selectedLanguageLevel) }