package ai.lingopulse.data.conversation.remote

actual fun readFileBytes(filePath: String): ByteArray {
    // TODO: Implement WASM JS file reading
    return byteArrayOf()
}

actual fun getFileName(filePath: String): String {
    return filePath.substringAfterLast("/")
}