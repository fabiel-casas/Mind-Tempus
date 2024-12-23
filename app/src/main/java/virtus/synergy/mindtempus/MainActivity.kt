package virtus.synergy.mindtempus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import org.koin.androidx.compose.KoinAndroidContext
import virtus.synergy.design_system.theme.MindTempusTheme
import virtus.synergy.mindtempus.navigation.MainNavigationHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MindTempusTheme {
                KoinAndroidContext {
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
