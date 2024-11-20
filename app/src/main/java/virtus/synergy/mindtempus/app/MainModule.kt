package virtus.synergy.mindtempus.app

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import virtus.synergy.core.NotificationCreator
import virtus.synergy.mindtempus.notifications.NotificationScheduler

/**
 * MindTempus
 * Created on 02/07/2023.
 * Author: johan
 */
val mainModule = module {
    single {
        CoroutineScope(SupervisorJob() + Dispatchers.IO)
    }
    factory<SharedPreferences> {
        androidContext().getSharedPreferences("app.conf", Context.MODE_PRIVATE)
    }
    factory<NotificationCreator> {
        NotificationScheduler(get(), get())
    }
}