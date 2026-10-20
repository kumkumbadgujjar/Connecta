package com.example.connecta.presentation.navigation


import kotlinx.serialization.Serializable
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.connecta.presentation.callscreen.CallScreen
import com.example.connecta.presentation.homescreen.HomeScreen
import com.example.connecta.presentation.postscreen.PostScreen
import com.example.connecta.presentation.splashscreen.SplashScreen
import com.example.connecta.presentation.updatescreen.UpdateScreen
import com.example.connecta.presentation.userregistrationscreen.UserRegistrationScreen
import com.example.connecta.presentation.welcomescreen.WelcomeScreen

@Serializable
data object PostScreen:Routes()


@Composable
fun ConnectaNavigationScreen (){

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.SplashScreen){

        composable<Routes.SplashScreen>{
            SplashScreen(navController)
        }

        composable<Routes.WelcomeScreen>{
            WelcomeScreen(navController)
        }

        composable<Routes.UserRegistrationScreen>{
            UserRegistrationScreen(navController)
        }

        composable<Routes.HomeScreen>{
            HomeScreen()
        }

        composable<Routes.UpdateScreen>{
            UpdateScreen()
        }

        composable<Routes.PostScreen>{
            PostScreen()
        }

        composable<Routes.CallScreen>{
            CallScreen()
        }



    }
}