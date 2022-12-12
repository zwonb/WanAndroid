package me.zwonb.wanandroid.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import me.zwonb.wanandroid.ui.home.HomePage
import me.zwonb.wanandroid.ui.login.LoginPage
import me.zwonb.wanandroid.ui.me.MePage
import me.zwonb.wanandroid.ui.square.SquarePage

@Composable
fun WanNavHost(navController: NavHostController, modifier: Modifier) {
    NavHost(navController, home, modifier) {
        composable(home) { HomePage(navController) }
        composable(square) { SquarePage(navController) }
        composable(me) { MePage(navController) }
        composable(login) { LoginPage(navController) }
    }
}