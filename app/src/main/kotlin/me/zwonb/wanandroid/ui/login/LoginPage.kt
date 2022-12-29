package me.zwonb.wanandroid.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import me.zwonb.wanandroid.ui.home.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPage(navController: NavHostController, viewModel: LoginViewModel = hiltViewModel()) {
    val backStackEntry = remember(navController) {
        navController.previousBackStackEntry
    }
    val homeViewModel = hiltViewModel<HomeViewModel>(backStackEntry!!)
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(viewModel.event) {
        viewModel.event.collect {
            if (it?.first == 0) {
                snackbarHostState.showSnackbar(it.second)
            } else if (it?.first == 1) {
                homeViewModel.refresh()
                navController.popBackStack()
            }
        }
    }
    Scaffold(
        Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(
                    text = "登录",
                    style = MaterialTheme.typography.titleMedium
                )
            }, navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.Close, null)
                }
            })
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                Modifier.imePadding()
            )
        }
    ) {
        Column(
            Modifier
                .padding(it)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = viewModel.state.username,
                onValueChange = viewModel::changeUsername,
                placeholder = { Text(text = "用户名") }
            )
            Spacer(Modifier.padding(top = 12.dp))
            OutlinedTextField(
                value = viewModel.state.password, onValueChange = viewModel::changePassword,
                placeholder = { Text(text = "密码") },
                visualTransformation = PasswordVisualTransformation()
            )
            Button(
                onClick = viewModel::login,
                Modifier.padding(top = 12.dp),
                enabled = !viewModel.state.loading
            ) {
                Text(text = "登录")
            }
        }
    }
}