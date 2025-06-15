package ai.lingopulse.util.extension

fun Int.toColorHexString(includeAlpha: Boolean = true): String {
    val argb = this.toUInt()
    return if (includeAlpha) {
        "#${argb.toString(16).padStart(8, '0').uppercase()}"
    } else {
        "#${(argb and 0xFFFFFFu).toString(16).padStart(6, '0').uppercase()}"
    }
}

fun Int.isColorDark(): Boolean {
    val red = (this shr 16) and 0xFF
    val green = (this shr 8) and 0xFF
    val blue = this and 0xFF

    val darkness = 1 - (0.299 * red + 0.587 * green + 0.114 * blue) / 255
    return darkness >= 0.5
}

fun Int.getOverlayColor(): Int {
    return if (this.isColorDark()) {
        0xFFACACAC.toInt() // Light overlay (white)
    } else {
        0xFF222222.toInt() // Dark overlay (black)
    }
}

fun Int.getDarker(): Int {
    val alpha = (this shr 24) and 0xFF
    val red = ((this shr 16) and 0xFF) * 0.8
    val green = ((this shr 8) and 0xFF) * 0.8
    val blue = (this and 0xFF) * 0.8

    return ((alpha shl 24) or
            (red.toInt().coerceIn(0, 255) shl 16) or
            (green.toInt().coerceIn(0, 255) shl 8) or
            (blue.toInt().coerceIn(0, 255)))
}