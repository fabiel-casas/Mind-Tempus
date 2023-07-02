package virtus.synergy.design_system.theme

import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 *
 * Created on 10/05/2023.
 */

@Composable
fun getCardElevation(defaultElevation: Dp = 3.dp): CardElevation {
    return CardDefaults.cardElevation(
        defaultElevation = defaultElevation,
        pressedElevation = defaultElevation,
        focusedElevation = defaultElevation,
        hoveredElevation = defaultElevation,
        draggedElevation = defaultElevation,
        disabledElevation = defaultElevation,
    )
}
