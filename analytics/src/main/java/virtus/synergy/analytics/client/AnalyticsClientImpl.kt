package virtus.synergy.analytics.client

import android.os.Bundle
import android.util.Log
import virtus.synergy.analytics.AnalyticsEvent
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

class AnalyticsClientImpl : AnalyticsClient {

    private val firebase: FirebaseAnalytics = Firebase.analytics

    init {
        firebase.setUserId("guest")
    }

    override fun trackEvent(name: String, properties: Map<String, String>) {
        val bundle = Bundle()
        properties.forEach { (key, value) ->
            bundle.putString(key, value)
        }
        firebase.logEvent(name, bundle)
        Log.i("MT${this::class.simpleName}", "📌 Tracked event $name, $properties $bundle")
    }

    override fun trackEvent(event: AnalyticsEvent) {
        trackEvent(name = event.eventName, properties = event.properties)
    }

}
