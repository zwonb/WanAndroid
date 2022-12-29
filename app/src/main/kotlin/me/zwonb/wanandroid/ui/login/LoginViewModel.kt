package me.zwonb.wanandroid.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import me.zwonb.wanandroid.data.Repository
import me.zwonb.wanandroid.network.msg
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    var state by mutableStateOf(LoginState())
        private set
    var event = MutableSharedFlow<Pair<Int, String>?>()
        private set

    fun changeUsername(username: String) {
        state = state.copy(username = username)
    }

    fun changePassword(password: String) {
        state = state.copy(password = password)
    }

    fun login() {
        repository.login(state.username, state.password)
            .onStart {
                state = state.copy(loading = true)
            }.onEach {
                event.emit(Pair(1, ""))
            }.onCompletion {
                state = state.copy(loading = false)
            }.catch {
                event.emit(Pair(0, it.msg))
            }.launchIn(viewModelScope)
    }

}

data class LoginState(
    val username: String = "",
    val password: String = "",
    val loading: Boolean = false
)