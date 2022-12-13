package me.zwonb.wanandroid.data

import androidx.paging.PagingSource
import androidx.paging.PagingState

class BasePagingSource<T : Any>(private val request: suspend (page: Int) -> List<T>) :
    PagingSource<Int, T>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        return try {
            val page = params.key ?: PAGE_START
            val list = request(page)
            if (page == PAGE_START && list.isEmpty()) {
                LoadResult.Error(Throwable("无数据"))
            } else {
                val nextKey = if (list.isNotEmpty()) page + 1 else null
                LoadResult.Page(list, null, nextKey)
            }
        } catch (e: Throwable) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? = null
}

const val PAGE_START = 0
const val PAGE_SIZE = 20