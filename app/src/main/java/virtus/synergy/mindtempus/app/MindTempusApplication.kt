package virtus.synergy.mindtempus.app

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import virtus.synergy.analytics.AnalyticsHandler
import virtus.synergy.journal.journalModule

/**
 * MindTempus
 * Created on 02/07/2023.
 * Author: johan
 */
class MindTempusApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        MobileAds.initialize(this)
        AnalyticsHandler.init()
        GlobalContext.startKoin {
            // Log Koin into Android logger
            androidLogger()
            // Reference Android context
            androidContext(this@MindTempusApplication)
            // Load modules
            modules(mainModule, journalModule)
        }
    }
}