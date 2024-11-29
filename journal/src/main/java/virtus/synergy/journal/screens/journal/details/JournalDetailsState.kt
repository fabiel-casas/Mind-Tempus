package virtus.synergy.journal.screens.journal.details

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import kotlinx.serialization.Serializable
import java.util.UUID

data class JournalDetailsState(
    val journalInfo: State<JournalInfo> = mutableStateOf(JournalInfo()),
)

data class JournalInfo(
    val title: String = "",
    val paragraph: List<Paragraph> = emptyList(),
    val emoji: String = "",
    val emotionalIndex: Int? = null,
)

@Serializable
data class Paragraph(
    val index: String = UUID.randomUUID().toString(),
    val data: String,
    val isFocused: Boolean = false,
    val isTitle: Boolean = false,
    val isBold: Boolean = false,
    val isItalic: Boolean = false,
)

sealed class JournalParagraphTools(
    val icon: Int,
    val title: String,
) {
    object Title : JournalParagraphTools(
//        icon = R.drawable.ic_title,
        icon = 0,
        title = "Title",
    )

    object Bold : JournalParagraphTools(
//        icon = R.drawable.ic_bold,
        icon = 0,
        title = "Bold",
    )

    object Italic : JournalParagraphTools(
//        icon = R.drawable.ic_italic,
        icon = 0,
        title = "Italic",
    )
}

val journalTools = listOf(
    JournalParagraphTools.Title,
    JournalParagraphTools.Bold,
    JournalParagraphTools.Italic,
)