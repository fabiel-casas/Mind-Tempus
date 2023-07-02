package virtus.synergy.journal

import androidx.compose.runtime.mutableStateOf
import virtus.synergy.journal.extensions.toEmotionEmoji
import virtus.synergy.journal.screens.journal.list.EmotionalListState
import virtus.synergy.journal.screens.journal.list.JournalItemState

/**
 *
 * Created on 08/05/2023.
 */
object Constants {
    const val PERMISSIONS_REQUEST_CODE = 23897
    const val NOTIFICATION_TIME = "notification_time"
    const val EMOJI_TIME = "emoji_time"

    const val JOURNAL_CHANNEL_ID = "journal"
    const val JOURNAL_NOTIFICATION_ID = 1
    const val JOURNAL_REQUEST_CODE = 1002
    const val JOURNAL_REQUEST_ACTION = "tempus.mind.REMINDER"

    /**
     * Emotional map represent the cluster and emotion sensation. the integer represent the emotional level
     * and the list of string contains the variety of emotions in that cluster.
     */
    val clusterOfEmotionsMap = mapOf<Int, List<String>>(
        1 to listOf(
            "\uD83D\uDE20", //Angry face
            "\uD83D\uDE21", //Pouting face
            "\uD83E\uDD2C", //Face with symbols on mouth
            "\uD83D\uDE2B", //Tired face
            "\uD83D\uDE29", //Weary face
            "\uD83D\uDE24", //Face with steam from nose
            "\uD83D\uDE31", //Face screaming in fear
            "\uD83D\uDE30", //Anxious face with sweat
            "\uD83D\uDE2D", //Loudly crying face
            "\uD83E\uDD22", //Nauseated face
            "\uD83E\uDD75", //Hot face
            "\uD83E\uDD76", //Cold face
        ),
        2 to listOf(
            "\uD83D\uDE12", //Unamused face
            "\uD83D\uDE1F", //Worried face
            "\uD83D\uDE14", //Pensive face
            "\uD83D\uDE23", //Persevering face
            "\uD83D\uDE16", //Confounded face
            "\uD83D\uDE28", //Fearful face
            "\uD83D\uDE22", //Crying face
            "\uD83D\uDE25", //Sad but relieved face
            "\uD83D\uDE35", //Dizzy face
            "\uD83E\uDD2F", //Exploding head
            "\uD83E\uDD12", //Face with Thermometer
        ),
        3 to listOf(
            // Neutral negative 3
            "\uD83D\uDE05", //Grinning face with sweat
            "\uD83D\uDE36", //Face without mouth
            "\uD83D\uDE10", //Neutral face
            "\uD83D\uDE11", //Expressionless face
            "\uD83D\uDE44", //Face with rolling eyes
            "\uD83E\uDD28", //Face with raised eyebrow
            "\uD83D\uDE1E", //Disappointed face
            "\uD83D\uDE15", //Confused face
            "\uD83D\uDE41", //Slightly frowning face
            "\u2639\uFE0F", //Frowning face
            "\uD83D\uDE2C", //Grimacing face
            "\uD83E\uDD7A", //Pleading face
            "\uD83D\uDE2F", //Hushed face
            "\uD83D\uDE26", //Frowning face with open mouth
            "\uD83D\uDE27", //Anguished face
            "\uD83D\uDE2A", //Sleepy face
            "\uD83D\uDE13", //Downcast face with sweat
            "\uD83E\uDD74", //Woozy face
            "\uD83D\uDE17", //Kissing face
            // Neutral positive 4
            "\uD83D\uDE42", //Slightly smiling face
            "\uD83D\uDE0C", //Relieved face
            "\uD83D\uDE1B", //Face with tongue
            "\uD83D\uDE0E", //Smiling face with sunglasses
            "\uD83E\uDDD0", //Face with monoculus
            "\uD83E\uDD20", //Cowboy hat face
            "\uD83E\uDD21", //Clown face
            "\uD83E\uDD14", //Thinking face
            "\uD83E\uDD2D", //Face with hand over mouth
            "\uD83D\uDE33", //Flushed face
            "\uD83D\uDE2E", //Face with open mouth
            "\uD83D\uDE32", //Astonished face
            "\uD83D\uDE34", //Sleepy Face
        ),
        4 to listOf(
            "\uD83D\uDE00", //Grinning face
            "\uD83D\uDE03", //Grinning face with big eyes
            "\uD83D\uDE07", //Smiling face with halo
            "\uD83D\uDE09", //Winking face
            "\uD83D\uDE0B", //Face savoring food
            "\uD83D\uDE19", //Kissing face with smiling eyes
            "\uD83D\uDE1C", //Winking face with tongue
            "\uD83D\uDE1D", //Squinting face with tongue
            "\uD83E\uDD17", //Hugging face
            "\uD83E\uDD13", //Nerd Face
        ),
        5 to listOf(
            "\uD83D\uDE01", //Beaming face with smiling eyes
            "\uD83D\uDE06", //Grinning squinting face
            "\uD83E\uDD23", //Rolling on the foor laughing
            "\u263A\uFE0F", //Smiling face
            "\uD83D\uDE0A", //Smiling face with smiling eyes
            "\uD83D\uDE0D", //Smiling face with heart-eyes
            "\uD83E\uDD70", //Smiling face with hearts
            "\uD83D\uDE18", //Face blowing a kiss
            "\uD83D\uDE1A", //Kissing face with closed eyes
            "\uD83E\uDD73", //Partying face
            "\uD83E\uDD29", //Star-struck
        ),
    )

    val emotionalListState = EmotionalListState(
        journalRecordList = mutableStateOf(
            mapOf(
                "April" to listOf(
                    JournalItemState(
                        id = "",
                        emotion = 2.toEmotionEmoji(),
                        dayOfTheWeek = "Mon",
                        time = "12",
                        monthYear = "April",
                        description = "January"
                    )
                )
            )
        )
    )
}