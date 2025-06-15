package ai.lingopulse.domain.common.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject

@Serializable
sealed class Content {
    @Serializable
    data class Text(val text: String) : Content() {
        override fun toString(): String {
            return "Content.Text(text='$text')"
        }
    }
    @Serializable
    data class ImageUrl(val url: String) : Content() {
        override fun toString(): String {
            return "Content.ImageUrl(url='$url')"
        }
    }
}

fun List<Content>.toJson(): String {
    val json = Json { prettyPrint = false }
    val jsonArray = buildJsonArray {
        this@toJson.forEach { content ->
            when (content) {
                is Content.Text -> {
                    add(buildJsonObject {
                        put("type", JsonPrimitive("Text"))
                        put("text", JsonPrimitive(content.text))
                    })
                }

                is Content.ImageUrl -> {
                    add(buildJsonObject {
                        put("type", JsonPrimitive("ImageUrl"))
                        put("url", JsonPrimitive(content.url))
                    })
                }
            }
        }
    }
    return json.encodeToString(JsonElement.serializer(), jsonArray)
}