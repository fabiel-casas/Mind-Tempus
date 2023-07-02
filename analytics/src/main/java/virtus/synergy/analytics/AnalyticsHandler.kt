package virtus.synergy.analytics

import virtus.synergy.analytics.client.AnalyticsClient
import virtus.synergy.analytics.client.AnalyticsClientImpl

object AnalyticsHandler {
    private lateinit var analyticsClient: AnalyticsClient

    fun init() {
        analyticsClient = AnalyticsClientImpl()
    }

    fun trackEvent(event: AnalyticsEvent) = analyticsClient.trackEvent(event)
}
