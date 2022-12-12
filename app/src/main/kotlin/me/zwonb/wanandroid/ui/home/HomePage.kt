package me.zwonb.wanandroid.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import me.zwonb.wanandroid.navigation.login

@Composable
fun HomePage(navController: NavHostController) {
    Text("home", Modifier.clickable { navController.navigate(login) })
}