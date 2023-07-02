package virtus.synergy.mindtempus.navigation

import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import virtus.synergy.journal.screens.journal.details.JournalEntryScreen
import virtus.synergy.journal.screens.journal.list.JournalListScreen
import virtus.synergy.journal.screens.journal.selector.EmotionsSelectorScreen
import virtus.synergy.journal.screens.settings.SettingsScreen

@Composable
fun MainNavigationHost(
    onOpenExternalActivity: (Intent) -> Unit,
    onOpenAd: () -> Unit,
) {
    val navController = rememberNavController()
    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = NavDestination.EmotionalList.route
    ) {
        composable(
            route = "${NavDestination.JournalEntry.route}/{journalId}",
            arguments = listOf(navArgument("journalId") { type = NavType.StringType })
        ) { backStackEntry ->
            JournalEntryScreen(
                onBackAction = {
                    onOpenAd()
                    navController.popBackStack()
                },
                journalId = backStackEntry.arguments?.getString("journalId").orEmpty()
            )
        }
        composable(route = NavDestination.EmotionalList.route) {
            JournalListScreen(
                navigateToCreateEntry = {
                    navController.navigate(route = NavDestination.EmotionalSelector.route)
                },
                navigateToSetting = {
                    navController.navigate(route = NavDestination.SettingScreen.route)
                },
                navigateToJournal = { journalId ->
                    navController.navigate(
                        route = NavDestination.JournalEntry.withPathParameters(journalId),
                        builder = {
                            argument("", argumentBuilder = {

                            })
                        }
                    )
                }
            )
        }
        composable(route = NavDestination.SettingScreen.route) {
            SettingsScreen(
                onBackAction = {
                    navController.popBackStack()
                },
                onOpenExternalActivity = onOpenExternalActivity
            )
        }
        composable(
            route = NavDestination.EmotionalSelector.route,
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = ".*/${NavDestination.EmotionalSelector.route}"
                }
            )
        ) {
            EmotionsSelectorScreen(
                onBackAction = {
                    navController.popBackStack()
                },
                onNavigateToJournal = { journalId ->
                    navController.navigate(
                        route = NavDestination.JournalEntry.withPathParameters(journalId),
                        navOptions = NavOptions.Builder()
                            .setPopUpTo(NavDestination.EmotionalSelector.route, inclusive = true)
                            .build()
                    )
                }
            )
        }
    }
}
