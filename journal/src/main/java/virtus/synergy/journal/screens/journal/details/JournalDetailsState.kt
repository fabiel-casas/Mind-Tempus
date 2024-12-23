package virtus.synergy.journal.screens.journal.details

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import kotlinx.serialization.Serializable
import virtus.synergy.design_system.R
import java.util.UUID

data class JournalDetailsState(
    val journalInfo: State<JournalInfo> = mutableStateOf(JournalInfo()),
    val paragraphTools: State<List<JournalParagraphToolsState>> = mutableStateOf(emptyList()),
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
    @DrawableRes val icon: Int,
    @StringRes val title: Int,
) {
    object Title : JournalParagraphTools(
        icon = R.drawable.ic_text_formatting_title,
        title = R.string.tools_text_formating_title,
    )

    object Bold : JournalParagraphTools(
        icon = R.drawable.ic_text_formatting_bold,
        title = R.string.tools_text_formating_bold,
    )

    object Italic : JournalParagraphTools(
        icon = R.drawable.ic_text_formatting_italic,
        title = R.string.tools_text_formating_italic,
    )
}

data class JournalParagraphToolsState(
    val type: JournalParagraphTools,
    val isSelected: Boolean = false,
    val isVisible: Boolean = true,
)

val journalTools = listOf(
    JournalParagraphToolsState(JournalParagraphTools.Title),
    JournalParagraphToolsState(JournalParagraphTools.Bold),
    JournalParagraphToolsState(JournalParagraphTools.Italic),
)