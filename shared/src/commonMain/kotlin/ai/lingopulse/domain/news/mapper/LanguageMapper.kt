package ai.lingopulse.domain.news.mapper

import ai.lingopulse.shared.LANGUAGE_LEVELS
import ai.lingopulse.shared.SUPPORTING_LANGUAGES

fun String.toLanguageDisplayName(): String {
    return SUPPORTING_LANGUAGES[this] ?: this
}

fun String.toLanguageLevelDisplayName(): String {
    return LANGUAGE_LEVELS[this] ?: this
}