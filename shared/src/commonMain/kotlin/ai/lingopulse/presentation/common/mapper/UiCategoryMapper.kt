package ai.lingopulse.presentation.common.mapper

import ai.lingopulse.domain.common.model.Category
import ai.lingopulse.presentation.common.model.UiCategory

fun Category.toUiCategory(selected: Boolean) = UiCategory(
    id = this.id,
    category = this.category,
    isSelected = selected
)

fun List<Category>.toUiCategories(selectedCategories: List<String> = emptyList()) =
    this.map { it.toUiCategory(it.id in selectedCategories) }