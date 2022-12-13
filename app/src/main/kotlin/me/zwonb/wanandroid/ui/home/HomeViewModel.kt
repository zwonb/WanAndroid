package me.zwonb.wanandroid.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import me.zwonb.wanandroid.data.BasePagingSource
import me.zwonb.wanandroid.data.Repository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val flow = Pager(PagingConfig(20, prefetchDistance = 1)) {
        BasePagingSource { repository.homeData(it).data.datas }
    }.flow.cachedIn(viewModelScope)

}