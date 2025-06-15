package ai.lingopulse.presentation.common.mapper

import ai.lingopulse.domain.common.model.Language
import ai.lingopulse.presentation.setting.model.UiLanguage

fun Language.toUiLanguage(selected: Boolean) = UiLanguage(
    id = this.id,
    name = this.name,
    isSelected = selected
)

fun List<Language>.toUiLanguages(selectedLanguages: String = "") =
    this.map { it.toUiLanguage(it.id == selectedLanguages) }