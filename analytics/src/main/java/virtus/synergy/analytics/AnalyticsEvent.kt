package virtus.synergy.analytics

data class AnalyticsEvent(
    private val flow: String,
    private val screen: String,
    private val element: String,
    private val action: AnalyticsAction,
    private val elementType: ElementType,
    private val extraProperties: Map<String, String>? = null,
) {
    val eventName: String
        get() {
            val name = "${screen}_" +
                    "${action.value}_" +
                    element
            return try {
                name.substring(0, 40)
            } catch (e: Exception) {
                name
            }
        }
    val properties: Map<String, String>
        get() {
            return mapOf(
                "flow" to flow,
                "screen" to screen,
                "action" to action.name.lowercase(),
                "element_type" to elementType.name.lowercase(),
                "element_name" to element,
            ).plus(extraProperties ?: mapOf())
        }
}
