package nikmax.shoppinglist

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import nikmax.shopinglist.home.HomeScreen
import nikmax.shopinglist.list.ListScreen

@Composable
internal fun MainNavHost() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.Home
    ) {
        composable<Routes.Home> {
            HomeScreen(
                onNavigateToListDetails = { list ->
                    navController.navigate(Routes.List(list.id))
                }
            )
        }
        composable<Routes.List>(
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left)
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
            }
        ) {
            val args = it.toRoute<Routes.List>()
            ListScreen(
                listId = args.listId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
