package virtus.synergy.analytics.client

import virtus.synergy.analytics.AnalyticsEvent

interface AnalyticsClient {
    fun trackEvent(name: String, properties: Map<String, String>)
    fun trackEvent(event: AnalyticsEvent)
}