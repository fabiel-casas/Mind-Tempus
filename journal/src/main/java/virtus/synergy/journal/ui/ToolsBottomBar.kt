package virtus.synergy.journal.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import virtus.synergy.analytics.ui.elementTag
import virtus.synergy.design_system.components.MTIconButton
import virtus.synergy.design_system.theme.MindTempusTheme
import virtus.synergy.journal.screens.journal.details.JournalParagraphToolsState
import virtus.synergy.journal.screens.journal.details.journalTools

@Composable
internal fun TextEditorTools(
    modifier: Modifier,
    journalToolsState: List<JournalParagraphToolsState>,
    onToolAction: (JournalParagraphToolsState) -> Unit,
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .elementTag("bottomBar")
    ) {
        items(journalToolsState) { toolState ->
            val contentDescription = stringResource(id = toolState.type.title)
            val selectedColor = if (toolState.isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.surface
            }
            val animatedColor by animateColorAsState(
                selectedColor,
                label = "toolAnimationColor"
            )
            Box(
                modifier = Modifier.requiredSize(48.dp)
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .drawBehind {
                            drawCircle(
                                color = animatedColor,
                                radius = 24.dp.toPx(),
                                center = center
                            )
                        },
                )
                MTIconButton(
                    modifier = Modifier.elementTag(contentDescription),
                    onClick = { onToolAction(toolState) }
                ) {
                    Icon(
                        painter = painterResource(toolState.type.icon),
                        contentDescription = contentDescription,
                        tint = contentColorFor(selectedColor)
                    )
                }
            }
        }
    }
}

@PreviewScreenSizes
@Composable
private fun TextEditorToolsPreview() {
    MindTempusTheme {
        TextEditorTools(
            modifier = Modifier,
            journalToolsState = journalTools,
            onToolAction = {}
        )
    }
}