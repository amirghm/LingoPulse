package ai.lingopulse.data.conversation.remote

import java.io.File

actual fun readFileBytes(filePath: String): ByteArray {
    return File(filePath).readBytes()
}

actual fun getFileName(filePath: String): String {
    return File(filePath).name
}