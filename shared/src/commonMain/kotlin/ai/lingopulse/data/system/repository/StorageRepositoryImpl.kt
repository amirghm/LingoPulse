package ai.lingopulse.data.system.repository

import ai.lingopulse.domain.system.StorageRepository
import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set

class StorageRepositoryImpl(
    private val preference: Settings,
) : StorageRepository {

    companion object{
        private const val USER_LEARNING_LANGUAGE = "user_learning_language"
        private const val USER_CATEGORIES = "user_categories"
        private const val USER_LANGUAGE_LEVEL = "user_language_level"
    }

    override fun getUserLearningLanguage() = preference[USER_LEARNING_LANGUAGE, "🇺🇸 English (US)"]

    override fun getUserCategories(): List<String> {
        preference.getString(USER_CATEGORIES, "").split(",").let {
            return if (it.first() == "") {
                emptyList()
            } else {
                it
            }
        }
    }

    override fun getUserLanguageLevel() = preference[USER_LANGUAGE_LEVEL, "A2 - Elementary"]

    override fun setUserLearningLanguage(language: String) {
        preference[USER_LEARNING_LANGUAGE] = language
    }

    override fun setUserCategories(categories: List<String>) {
        preference[USER_CATEGORIES] = categories.joinToString(",")
    }

    override fun setUserLanguageLevel(level: String) {
        preference[USER_LANGUAGE_LEVEL] = level
    }
}