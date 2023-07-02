package virtus.synergy.design_system.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import virtus.synergy.design_system.theme.MindTempusTheme
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

/**
 *
 * Created on 21/04/2023.
 */

@Composable
fun AdBottomBanner(
    modifier: Modifier,
    localAdUnitId: String = ""
) {
    val isInEditMode = LocalInspectionMode.current
    if (isInEditMode) {
        Text(
            modifier = modifier
                .fillMaxWidth()
                .background(Color.Red)
                .padding(horizontal = 2.dp, vertical = 6.dp),
            textAlign = TextAlign.Center,
            color = Color.White,
            text = "Advert Here",
        )
    } else {
        val deviceCurrentWidth = LocalConfiguration.current.screenWidthDp
        val applicationContext = LocalContext.current.applicationContext
        val context = LocalContext.current
        AndroidView(
            modifier = modifier
                .fillMaxWidth(),
            factory = {
                AdView(context).apply {
                    setAdSize(
                        AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                            applicationContext,
                            deviceCurrentWidth,
                        ),
                    )
                    try {
                        adUnitId = localAdUnitId
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    loadAd(AdRequest.Builder().build())
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AdBottomBannerPreview() {
    MindTempusTheme {
        AdBottomBanner(modifier = Modifier.fillMaxWidth())
    }
}