package ai.lingopulse.domain.system

interface StorageRepository {
    fun getUserLearningLanguage(): String
    fun getUserCategories(): List<String>
    fun getUserLanguageLevel(): String
    fun setUserLearningLanguage(language: String)
    fun setUserCategories(categories: List<String>)
    fun setUserLanguageLevel(level: String)
}