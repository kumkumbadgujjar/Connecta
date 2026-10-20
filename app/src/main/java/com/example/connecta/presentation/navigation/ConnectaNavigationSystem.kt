package com.example.connecta.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.connecta.presentation.callscreen.CallScreen
import com.example.connecta.presentation.homescreen.ChatDetailScreen
import com.example.connecta.presentation.homescreen.HomeScreen
import com.example.connecta.presentation.postscreen.PostScreen
import com.example.connecta.presentation.splashscreen.SplashScreen
import com.example.connecta.presentation.updatescreen.UpdateScreen
import com.example.connecta.presentation.userprofilescreen.UserProfileScreen
import com.example.connecta.presentation.userregistrationscreen.UserRegistrationScreen
import com.example.connecta.presentation.welcomescreen.WelcomeScreen

@Composable
fun ConnectaNavigationScreen() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.SplashScreen) {

        composable<Routes.SplashScreen> {
            SplashScreen(navController)
        }

        composable<Routes.WelcomeScreen> {
            WelcomeScreen(navController)
        }

        composable<Routes.UserRegistrationScreen> {
            UserRegistrationScreen(navController)
        }

        composable<Routes.UserProfileScreen> {
            UserProfileScreen(navController)
        }

        composable<Routes.HomeScreen> {
            HomeScreen(
                onChatClick = { convId, partnerName, partnerImage, partnerPhone ->
                    navController.navigate(
                        Routes.ChatDetailScreen(
                            conversationId = convId,
                            partnerName = partnerName,
                            partnerImage = partnerImage,
                            partnerPhone = partnerPhone
                        )
                    )
                },
                onUpdatesClick = {
                    navController.navigate(Routes.UpdateScreen) {
                        launchSingleTop = true
                    }
                },
                onPostsClick = {
                    navController.navigate(Routes.PostScreen) {
                        launchSingleTop = true
                    }
                },
                onCallsClick = {
                    navController.navigate(Routes.CallScreen) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<Routes.ChatDetailScreen> { backStackEntry ->
            val route = backStackEntry.toRoute<Routes.ChatDetailScreen>()
            ChatDetailScreen(
                conversationId = route.conversationId,
                partnerName = route.partnerName,
                partnerImage = route.partnerImage,
                partnerPhone = route.partnerPhone,
                navController = navController
            )
        }

        composable<Routes.UpdateScreen> {
            UpdateScreen(
                onChatsClick = {
                    navController.navigate(Routes.HomeScreen) {
                        launchSingleTop = true
                    }
                },
                onPostClick = {
                    navController.navigate(Routes.PostScreen) {
                        launchSingleTop = true
                    }
                },
                onCallsClick = {
                    navController.navigate(Routes.CallScreen) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<Routes.PostScreen> {
            PostScreen(
                onChatsClick = {
                    navController.navigate(Routes.HomeScreen) {
                        launchSingleTop = true
                    }
                },
                onUpdatesClick = {
                    navController.navigate(Routes.UpdateScreen) {
                        launchSingleTop = true
                    }
                },
                onCallsClick = {
                    navController.navigate(Routes.CallScreen) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<Routes.CallScreen> {
            CallScreen(
                onChatsClick = {
                    navController.navigate(Routes.HomeScreen) {
                        launchSingleTop = true
                    }
                },
                onUpdatesClick = {
                    navController.navigate(Routes.UpdateScreen) {
                        launchSingleTop = true
                    }
                },
                onPostsClick = {
                    navController.navigate(Routes.PostScreen) {
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}