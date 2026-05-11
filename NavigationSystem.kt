package Zomato.Copy

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun MyApp() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = NavigationProperties.LoginScreen.route
    ) {
        composable(route = NavigationProperties.LoginScreen.route) {
            LoginScreen(navController)
        }
        composable(route = NavigationProperties.RegisterScreen.route) {
            RegisterScreen(navController)
        }
        composable(route = NavigationProperties.MainScreen.route){
            MainScreen(navController)
        }
    }
}
