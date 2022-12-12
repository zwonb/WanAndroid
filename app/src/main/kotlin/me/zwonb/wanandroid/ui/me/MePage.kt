package me.zwonb.wanandroid.ui.me

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import me.zwonb.wanandroid.navigation.login

@Composable
fun MePage(navController: NavHostController) {
    Column {
        Text("me", Modifier.clickable { navController.navigate(login) })
    }
}