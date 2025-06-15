package ai.lingopulse.util.extension

fun String.toColorInt(): Int {
    val hex = this.removePrefix("#").uppercase()

    val argb = when (hex.length) {
        3 -> { // #RGB
            val r = hex[0].toString().repeat(2)
            val g = hex[1].toString().repeat(2)
            val b = hex[2].toString().repeat(2)
            "FF$r$g$b"
        }
        4 -> { // #ARGB
            val a = hex[0].toString().repeat(2)
            val r = hex[1].toString().repeat(2)
            val g = hex[2].toString().repeat(2)
            val b = hex[3].toString().repeat(2)
            "$a$r$g$b"
        }
        6 -> { // #RRGGBB
            "FF$hex"
        }
        8 -> { // #AARRGGBB
            hex
        }
        else -> "000" // Unknown color
    }

    return argb.toLong(16).toInt()
}