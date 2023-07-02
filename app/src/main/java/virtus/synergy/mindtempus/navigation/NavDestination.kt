package virtus.synergy.mindtempus.navigation

import virtus.synergy.design_system.R


sealed class NavDestination(
    val route: String
) {
    object JournalEntry : NavDestination("journalEntry")
    object EmotionalList : NavDestination("emotionalList")
    object SettingScreen : NavDestination("settingScreen")
    object EmotionalSelector : NavDestination("emotionalSelector")
    companion object {
        fun navigationList() = listOf(EmotionalList)
    }
}

fun NavDestination.bottomName(): Int? = when (this) {
    NavDestination.EmotionalList -> R.string.bottom_bar_journal_entries
    else -> null
}

fun NavDestination.bottomIcon(): Int? = when (this) {
    NavDestination.EmotionalList -> R.drawable.ic_home
    else -> null
}

fun NavDestination.withPathParameters(vararg params: String): String {
    val uri = android.net.Uri.Builder()
    uri.appendPath(route)
    params.forEach {
        uri.appendPath(it)
    }
    val path = uri.toString()
    return path.substring(1, path.length)
}
