package Zomato.Copy

sealed class NavigationProperties(val route : String) {
    object LoginScreen : NavigationProperties("loginscreen")
    object RegisterScreen : NavigationProperties("registerscreen")

    object MainScreen : NavigationProperties("mainscreen")
}

