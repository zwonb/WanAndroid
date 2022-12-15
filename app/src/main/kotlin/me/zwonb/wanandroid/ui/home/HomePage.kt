package me.zwonb.wanandroid.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import me.zwonb.wanandroid.data.bean.HomeBean

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(navController: NavHostController, viewModel: HomeViewModel = hiltViewModel()) {
    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = {
            Text(
                "首页",
                style = MaterialTheme.typography.titleMedium
            )
        })
    }) { padding ->
        val pagingItems = viewModel.flow.collectAsLazyPagingItems()
        pagingItems.PagingStateBody(Modifier.padding(padding)) {
            LazyColumn(
                Modifier.padding(padding),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val bannerList = viewModel.bannerList
                if (bannerList.isNotEmpty()) {
                    item(contentType = 1) { HomeBanner(bannerList, navController) }
                }
                items(pagingItems, key = { it.id }) { bean ->
                    bean?.let { ItemBody(it) }
                }
                pagingAppendItem(pagingItems)
            }
        }
    }
}

@Composable
private fun ListBody(
    modifier: Modifier,
    pagingItems: LazyPagingItems<HomeBean.Data>
) {
    LazyColumn(
        modifier,
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(pagingItems, key = { it.id }) { bean ->
            bean?.let { ItemBody(it) }
        }
        pagingAppendItem(pagingItems)
    }
}

@Composable
fun ItemBody(data: HomeBean.Data) {
    Surface(shadowElevation = 1.dp) {
        Row(Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(
                    data.title,
                    Modifier.fillMaxWidth(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "${data.author?.ifEmpty { data.shareUser }} ${data.niceDate}",
                    fontSize = 12.sp,
                    color = LocalContentColor.current.copy(ContentAlpha.disabled)
                )
            }
            IconButton({}) {
                Icon(Icons.Default.FavoriteBorder, null)
            }
        }
    }
}

@Composable
fun <T : Any> LazyPagingItems<T>.PagingStateBody(modifier: Modifier, content: @Composable () -> Unit) {
    when (val state = loadState.refresh) {
        is LoadState.Error -> RetryBox(modifier, state.error.message.toString()) { retry() }
        LoadState.Loading -> StateBox(modifier, "加载中...")
        is LoadState.NotLoading -> content()
    }
}

@Composable
fun StateBox(modifier: Modifier, text: String) {
    Box(modifier.fillMaxSize().padding(12.dp), Alignment.Center) {
        Text(text, fontSize = 18.sp)
    }
}

@Composable
fun RetryBox(modifier: Modifier, text: String, retry: () -> Unit) {
    Box(modifier.fillMaxSize().padding(12.dp), Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text, fontSize = 18.sp)
            Spacer(Modifier.height(6.dp))
            ElevatedButton(retry) {
                Text("重试")
            }
        }
    }
}

fun <T : Any> LazyListScope.pagingAppendItem(pagingItems: LazyPagingItems<T>) {
    when (val state = pagingItems.loadState.append) {
        is LoadState.Error -> item {
            Box(Modifier.fillMaxWidth(), Alignment.Center) {
                TextButton({ pagingItems.retry() }) {
                    Text("重试")
                }
            }
        }

        LoadState.Loading -> item {
            Box(Modifier.fillMaxWidth(), Alignment.Center) {
                CircularProgressIndicator(strokeWidth = 2.dp)
            }
        }

        is LoadState.NotLoading -> {
            if (state.endOfPaginationReached) {
                item {
                    Box(Modifier.fillMaxWidth(), Alignment.Center) {
                        Text("到底了")
                    }
                }
            }
        }
    }
}