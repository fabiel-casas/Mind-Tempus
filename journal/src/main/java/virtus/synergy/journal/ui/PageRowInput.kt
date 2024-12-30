package virtus.synergy.journal.ui

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import virtus.synergy.design_system.theme.MindTempusTheme
import virtus.synergy.journal.screens.journal.details.Paragraph

/**
 * Page row input is a composable that represents a row of text input. It is used to display a paragraph
 * and allows the user to edit the text. It supports formatting options like bold and italic. And
 * also supports adding new rows by pressing the enter key. This paragraph also supports Title formatting.
 *
 * @param modifier
 * @param isFocused
 * @param isTitle
 * @param textField
 * @param onParagraphTextChanged
 * @param onParagraphFocusChanged
 * @param onAddNewRow
 */
@Composable
fun PageRowInput(
    modifier: Modifier,
    isFocused: Boolean = false,
    isTitle: Boolean = false,
    textField: TextFieldValue,
    onParagraphTextChanged: (textField: TextFieldValue) -> Unit,
    onParagraphFocusChanged: (isFocused: Boolean) -> Unit,
    onAddNewRow: () -> Unit,
) {
    Row(
        modifier = modifier,
    ) {
        val focusRequester = remember { FocusRequester() }
        LaunchedEffect(isFocused) {
            if (isFocused) {
                focusRequester.requestFocus()
            }
        }
        val textStyle = if (isTitle) {
            MaterialTheme.typography.titleLarge
        } else {
            MaterialTheme.typography.bodyLarge
        }
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    onParagraphFocusChanged(focusState.isFocused)
                },
            value = textField,
            onValueChange = { newTextField ->
                Log.d(
                    "TAG",
                    "Text Modification 1 ${newTextField.text.length} $newTextField: ${newTextField.text}"
                )
                onParagraphTextChanged(
                    newTextField
                )
            },
            placeholder = {},
            textStyle = textStyle,
            colors = TextFieldDefaults.paragraphInputColors(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Go
            ),
            visualTransformation = markdownVisualTransformation(),
            keyboardActions = KeyboardActions(
                onGo = {
                    onAddNewRow()
                },
            ),
        )
    }
}

@Composable
private fun TextFieldDefaults.paragraphInputColors() = colors(
    focusedContainerColor = Color.Transparent,
    disabledContainerColor = Color.Transparent,
    unfocusedContainerColor = Color.Transparent,
    errorContainerColor = Color.Transparent,
    focusedIndicatorColor = Color.Transparent,
    disabledIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
)

@Preview
@Composable
private fun PageRowInputPreview() {
    val text = remember {
        Paragraph(
            data = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
            textFieldValue = TextFieldValue(""),
        )
    }
    MindTempusTheme {
        Surface {
            PageRowInput(
                modifier = Modifier.fillMaxWidth(),
                textField = text.textFieldValue,
                isTitle = false,
                isFocused = false,
                onParagraphTextChanged = { _ -> },
                onParagraphFocusChanged = { _ -> },
                onAddNewRow = { }
            )
        }
    }
}