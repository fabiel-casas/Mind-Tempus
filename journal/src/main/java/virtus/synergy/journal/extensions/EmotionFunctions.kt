package virtus.synergy.journal.extensions

/**
 *
 * Created on 01/05/2023.
 */

fun Int.toEmotionEmoji(): String {
    return when (this) {
        1 -> "\uD83D\uDE1E"
        2 -> "☹️"
        3 -> "\uD83D\uDE10"
        4 -> "\uD83D\uDE42"
        5 -> "\uD83D\uDE04"
        else -> ""
    }
}