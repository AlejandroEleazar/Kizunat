package com.example.kizunat.NavigationWrapper

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
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

    NavHost(navController = navController, startDestination = Welcome){

        composable<Welcome>{
            welcome(
                navigateToLogIn = {
                    log -> navController.navigate(
                        LoginSignup( log = log)
                    )
                }
            )
        }

        composable<LoginSignup> { backStackEntry ->
            var detail = backStackEntry.toRoute<LoginSignup>()
            AuthScreen(
                auth,
                navigateToHome = {navController.navigate(Home)},
                navigateToForm = {
                    navController.navigate(Form)},
                detail.log
            )
        }

        composable<Form> {
            FormScreen(
                navigateToHome = {navController.navigate(Home)},
                db,
                //detail.name
            )
        }

        composable<Home> {
            HomeScreen(
                navigateToHome = {navController.navigate(Home)},
                navigateToMenu = {navController.navigate(Menu)},
                navigateToProfile = {navController.navigate(Profile)}
            )
        }

        composable<Menu> {
            MenuScreen(
                navigateToHome = {navController.navigate(Home)},
                navigateToMenu = {navController.navigate(Menu)},
                navigateToProfile = {navController.navigate(Profile)}
            )
        }

        composable<Profile> {
            ProfileScreen(
                navigateToHome = {navController.navigate(Home)},
                navigateToMenu = {navController.navigate(Menu)},
                navigateToProfile = {navController.navigate(Profile)},
                navigateToEditProfile = {navController.navigate(EditProfile)}
            )
        }

        composable<EditProfile> {
            EditProfileScreen(
                navigateBack = {navController.popBackStack()}
            )
        }
    }
}