package ai.lingopulse.util

import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString

suspend fun resolveStrings(vararg resources: StringResource): Map<String, String> {
    return resources.associate { it.key to getString(it) }
}