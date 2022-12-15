package me.zwonb.wanandroid.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.changedToDownIgnoreConsumed
import androidx.compose.ui.input.pointer.changedToUpIgnoreConsumed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.google.accompanist.pager.*
import kotlinx.coroutines.delay
import me.zwonb.wanandroid.data.bean.BannerBean

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeBanner(list: List<BannerBean>, nav: NavHostController) {
    Banner(list = list) { page, bean ->
        BannerItem(page = page, bean.imagePath) {
//            nav.navSingleTop("${Routes.WEB_VIEW}/使用说明/${OPERATION_INSTRUCTION.urlEncode()}")
        }
    }
}

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
private fun PagerScope.BannerItem(page: Int, img: String, onClick: () -> Unit) {
//    Card(
//        Modifier
//            .graphicsLayer {
//                val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue
//                lerp(
//                    start = ScaleFactor(0.95f, 0.85f),
//                    stop = ScaleFactor(1f, 1f),
//                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
//                ).also { scale ->
//                    scaleX = scale.scaleX
//                    scaleY = scale.scaleY
//                }
//                alpha = lerp(
//                    start = ScaleFactor(0.5f, 0.5f),
//                    stop = ScaleFactor(1f, 1f),
//                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
//                ).scaleX
//            }
//            .fillMaxWidth()
//            .aspectRatio(3.45f),
//        shape = RoundedCornerShape(8.dp),
//    ) {
        AsyncImage(
            img, null,
            Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
                .aspectRatio(2f)
                .clickable(onClick = onClick),
//            placeholder = painterResource(id = R.drawable.pic_banner_placeholder),
//            error = painterResource(id = R.drawable.pic_banner_placeholder),
            contentScale = ContentScale.Crop
        )
//    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun <T> Banner(list: List<T>, content: @Composable PagerScope.(page: Int, bean: T) -> Unit) {
    val pageCount = list.size
    val loopingCount = Int.MAX_VALUE
    val startIndex = loopingCount / 2
    val state = rememberPagerState(startIndex)
    fun pageMapper(index: Int): Int {
        return (index - startIndex).floorMod(pageCount)
    }

    val modifier = Modifier
        .fillMaxWidth()
        .autoScrollPage(state, pageCount > 1)

    Box(Modifier.fillMaxWidth()) {
        HorizontalPager(
            loopingCount, modifier, state,
            itemSpacing = 8.dp, contentPadding = PaddingValues(horizontal = 0.dp)
        ) {
            val page = pageMapper(it)
            content(it, list[page])
        }
        if (list.size > 1) {
            HorizontalPagerIndicator(
                pagerState = state,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp),
                pageCount = list.size,
                pageIndexMapping = ::pageMapper,
                activeColor = MaterialTheme.colors.primary
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
private fun Modifier.autoScrollPage(state: PagerState, enabled: Boolean = true): Modifier =
    composed {
        if (enabled) {
            var autoScroll by remember { mutableStateOf(enabled) }
            LaunchedEffect(autoScroll) {
                while (autoScroll) {
                    delay(2000)
                    try {
                        state.animateScrollToPage(state.currentPage + 1)
                    } catch (e: Exception) {
//                        loge("autoScrollPage", e)
                    }
                }
            }
            pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        awaitPointerEvent(PointerEventPass.Initial).changes
                            .firstOrNull()
                            ?.let {
                                when {
                                    it.changedToDownIgnoreConsumed() -> autoScroll = false
                                    it.changedToUpIgnoreConsumed() -> {
//                                        loge("changedToUpIgnoreConsumed")
                                        if (autoScroll) { // 多指的情况
                                            autoScroll = false
                                        }
                                        autoScroll = true
                                    }
                                }
                            }
                    }
                }
            }
        } else {
            this
        }
    }

private fun Int.floorMod(other: Int): Int = when (other) {
    0 -> this
    else -> this - floorDiv(other) * other
}
