package ai.lingopulse.util.extension

import kotlinx.datetime.Instant
import nl.jacobras.humanreadable.HumanReadable

fun Long.toInstant(): Instant = Instant.fromEpochMilliseconds(this)

fun Long.toHumanReadable() = HumanReadable.timeAgo(this.toInstant())