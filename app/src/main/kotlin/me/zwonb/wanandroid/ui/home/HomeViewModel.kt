package me.zwonb.wanandroid.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import me.zwonb.wanandroid.data.BasePagingSource
import me.zwonb.wanandroid.data.Repository
import me.zwonb.wanandroid.data.bean.BannerBean
import me.zwonb.wanandroid.data.bean.HomeBean
import me.zwonb.wanandroid.network.hasCookie
import me.zwonb.wanandroid.network.msg
import me.zwonb.wanandroid.util.logE
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val flow = Pager(PagingConfig(20, prefetchDistance = 1)) {
        BasePagingSource { repository.homeData(it).data!!.datas }
    }.flow.cachedIn(viewModelScope)

    var state by mutableStateOf(HomeState())
        private set
    var refresh = MutableSharedFlow<Unit>()

    init {
        banner()
    }

    private fun banner() {
        repository.banner().onEach {
            state = state.copy(banner = it.data!!)
        }.catch {
            logE("banner", it)
        }.launchIn(viewModelScope)
    }

    fun collect(data: HomeBean.Data?) {
        data ?: return
        if (hasCookie()) {
            repository.collect(data.id).onEach {
                logE(it.toString())
            }.catch {
                logE(it.msg)
            }.launchIn(viewModelScope)
        } else {
            state = state.copy(loginDialog = true)
        }
    }

    fun dismissLogin() {
        state = state.copy(loginDialog = false)
    }

    fun refresh() {
        viewModelScope.launch {
            refresh.emit(Unit)
        }
    }

}

data class HomeState(
    val banner: List<BannerBean> = emptyList(), val loginDialog: Boolean = false,
)