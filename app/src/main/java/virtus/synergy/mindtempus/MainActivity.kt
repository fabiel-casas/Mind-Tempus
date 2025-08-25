package virtus.synergy.mindtempus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import org.koin.androidx.compose.KoinAndroidContext
import virtus.synergy.design_system.theme.MindTempusTheme
import virtus.synergy.mindtempus.navigation.MainNavigationHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
//            statusBarStyle = SystemBarStyle.auto(
//                Color.Transparent.toArgb(), // Light scrim (effectively the background when opaque content doesn't reach)
//                Color.Transparent.toArgb(), // Dark scrim (effectively the background)
//            ),
//            navigationBarStyle = SystemBarStyle.auto(
//                Color.Transparent.toArgb(), // Light scrim/background
//                Color.Transparent.toArgb()  // Dark scrim/background
//            )
        )
        setContent {
            MindTempusTheme {
                KoinAndroidContext {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    ) {
                        MainNavigationHost(
                            onOpenExternalActivity = {
                                startActivity(it)
                            },
                            onOpenAd = {

                            }
                        )
                    }
                }
            }
        }
    }
}
