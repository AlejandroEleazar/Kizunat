package com.example.kizunat.NavigationWrapper

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kizunat.AppScreens.LogIn.AuthScreen
import com.example.kizunat.AppScreens.Profile.EditProfileScreen
import com.example.kizunat.AppScreens.LogIn.FormScreen
import com.example.kizunat.AppScreens.Home.HomeScreen
import com.example.kizunat.AppScreens.Menu.MenuScreen
import com.example.kizunat.AppScreens.Profile.ProfileScreen
import com.example.kizunat.AppScreens.Welcome.welcome
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun NavigationWrapper(auth: FirebaseAuth, db: FirebaseFirestore) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Welcome.route()) {

        composable(Welcome.route()) {
            welcome(
                navigateToLogIn = { log ->
                    navController.navigate(LoginSignup.createRoute(log))
                }
            )
        }

        composable(
            route = "loginSignup/{log}",
            arguments = listOf(navArgument("log") { type = NavType.BoolType })
        ) { backStackEntry ->
            val log = backStackEntry.arguments?.getBoolean("log") ?: false
            AuthScreen(
                auth = auth,
                navigateToHome = { navController.navigate(Home.route()) },
                navigateToForm = { name -> navController.navigate(Form.createRoute(name)) },
                log = log
            )
        }

        composable(
            route = "form/{name}",
            arguments = listOf(navArgument("name") { type = NavType.StringType })
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: ""
            FormScreen(
                navigateToHome = { navController.navigate(Home.route()) },
                db = db,
                name = name
            )
        }

        composable(Home.route()) {
            HomeScreen(
                db = db,
                navigateToHome = { navController.navigate(Home.route()) },
                navigateToMenu = { navController.navigate(Menu.route()) },
                navigateToProfile = { navController.navigate(Profile.route()) }
            )
        }

        composable(Menu.route()) {
            MenuScreen(
                navigateToHome = { navController.navigate(Home.route()) },
                navigateToMenu = { navController.navigate(Menu.route()) },
                navigateToProfile = { navController.navigate(Profile.route()) }
            )
        }

        composable(Profile.route()) {
            ProfileScreen(
                db = db,
                navigateToHome = { navController.navigate(Home.route()) },
                navigateToMenu = { navController.navigate(Menu.route()) },
                navigateToProfile = { navController.navigate(Profile.route()) },
                navigateToEditProfile = { navController.navigate(EditProfile.route()) }
            )
        }

        composable(EditProfile.route()) {
            EditProfileScreen(
                db = db,
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}

// Rutas

fun Welcome.route() = "welcome"

fun LoginSignup.route() = "loginSignup/${log}"

fun LoginSignup.Companion.createRoute(log: Boolean) = "loginSignup/$log"

fun Form.route() = "form/${name}"

fun Form.Companion.createRoute(name: String) = "form/$name"

fun Home.route() = "home"

fun Menu.route() = "menu"

fun Profile.route() = "profile"

fun EditProfile.route() = "editProfile"
