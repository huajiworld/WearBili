package cn.spacexc.wearbili.activity.user

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import cn.spacexc.wearbili.R
import cn.spacexc.wearbili.ui.*
import cn.spacexc.wearbili.ui.ModifierExtends.clickVfx
import cn.spacexc.wearbili.utils.NumberUtils.toShortChinese
import cn.spacexc.wearbili.utils.ifNullOrEmpty
import cn.spacexc.wearbili.utils.parseColor
import cn.spacexc.wearbili.viewmodel.UserSpaceViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

/* 
WearBili Copyright (C) 2022 XC
This program comes with ABSOLUTELY NO WARRANTY.
This is free software, and you are welcome to redistribute it under certain conditions.
*/

/*
 * Created by XC on 2022/11/25.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

class SpaceProfileActivity : AppCompatActivity() {
    val viewModel: UserSpaceViewModel by viewModels()

    @OptIn(ExperimentalPagerApi::class, ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userMid = intent.getLongExtra("userMid", 0)
        viewModel.getUser(userMid)
        viewModel.getVideos(userMid, true)
        viewModel.getDynamic(true, userMid)
        viewModel.checkSubscribe(userMid)
        viewModel.getUserFans(userMid)
        setContent {
            val localDensity = LocalDensity.current
            val user by viewModel.user.observeAsState()
            var pendentHeight by remember {
                mutableStateOf(0.dp)
            }
            val collapsingState = rememberCollapsingToolbarScaffoldState()
            val pagerState = rememberPagerState()
            val userVideos by viewModel.videos.observeAsState()
            val dynamicList by viewModel.dynamicItemList.observeAsState()
            val scope = rememberCoroutineScope()
            val isError by viewModel.isError.observeAsState()
            val isSubscribed by viewModel.isFollowed.observeAsState()
            val fans by viewModel.fans.observeAsState()
            val followButtonColor by animateColorAsState(
                targetValue = if (isSubscribed == true) Color(
                    63,
                    63,
                    63,
                    255
                ) else BilibiliPink, animationSpec = tween(durationMillis = 400)
            )
            var searchBoxKeyword by remember {
                mutableStateOf("")
            }
            CirclesBackground.RegularBackgroundWithTitleAndBackArrow(
                title = "个人空间",
                onBack = { finish() },
                isLoading = user == null || fans == null || isSubscribed == null || userVideos == null || dynamicList == null,
                isError = isError == true,
                errorRetry = {
                    viewModel.isError.value = false
                    viewModel.getUser(userMid)
                    viewModel.getVideos(userMid, true)
                    viewModel.getDynamic(true, userMid)
                    viewModel.checkSubscribe(userMid)
                }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Transparent)
                ) {
                    CollapsingToolbarScaffold(
                        state = collapsingState,
                        modifier = Modifier.fillMaxSize(),
                        scrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
                        toolbarModifier = Modifier.verticalScroll(rememberScrollState()),
                        toolbar = {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(0.dp)
                            )   //不！要！删！掉！这个是用来做伸缩topbar的！！很重要！


                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .parallax(0f)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 0.dp, horizontal = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    var avatarBoxSize by remember {
                                        mutableStateOf(0.dp)
                                    }
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .weight(2f)
                                            .onGloballyPositioned {
                                                avatarBoxSize =
                                                    with(localDensity) { it.size.height.toDp() }
                                            }
                                    ) {
                                        if (user?.data?.pendant?.image_enhance.isNullOrEmpty()) {
                                            AsyncImage(
                                                model = ImageRequest.Builder(LocalContext.current)
                                                    .data(user?.data?.face)
                                                    .placeholder(R.drawable.akari).crossfade(true)
                                                    .build(),
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(8.dp)
                                                    .clip(CircleShape)
                                                    .aspectRatio(1f)

                                            )
                                        } else {
                                            AsyncImage(
                                                model = ImageRequest.Builder(LocalContext.current)
                                                    .data(user?.data?.face)
                                                    .crossfade(true).placeholder(R.drawable.akari)
                                                    .build(),
                                                contentDescription = null,
                                                modifier = Modifier
                                                    //.fillMaxWidth()
                                                    .size(
                                                        pendentHeight.times(0.6f),
                                                        pendentHeight.times(0.6f)
                                                    )
                                                    .clip(CircleShape)
                                                    .aspectRatio(1f)
                                            )
                                            AsyncImage(model = ImageRequest.Builder(LocalContext.current)
                                                .data(user?.data?.pendant?.image_enhance)
                                                .crossfade(true)
                                                .build(),
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .onGloballyPositioned {
                                                        pendentHeight = with(localDensity) {
                                                            it.size.height.toDp()
                                                        }
                                                    }
                                                    .aspectRatio(1f)
                                            )

                                        }
                                        if (user?.data?.official?.type != OFFICIAL_TYPE_NONE) {
                                            Image(
                                                painter = painterResource(
                                                    id = when (user?.data?.official?.type) {
                                                        OFFICIAL_TYPE_ORG -> R.drawable.flash_business
                                                        OFFICIAL_TYPE_PERSONAL -> R.drawable.flash_personal
                                                        else -> 0
                                                    }
                                                ),
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .size(avatarBoxSize.times(0.25f))
                                                    .align(Alignment.BottomEnd)
                                                    .offset(
                                                        x = (-8).dp,
                                                        y = (-8).dp
                                                    )
                                            )
                                        }
                                    }
                                    Column(
                                        modifier = Modifier
                                            .weight(4f)
                                            .fillMaxWidth()
                                    ) {
                                        var maxLines by remember {
                                            mutableStateOf(1)
                                        }
                                        Text(
                                            text = user?.data?.name ?: "加载中",
                                            fontFamily = puhuiFamily,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = parseColor(user?.data?.vip?.nickname_color.ifNullOrEmpty { "#FFFFFF" })
                                            //modifier = Modifier.scale(collapsingState.toolbarState.progress)
                                        )
                                        if (!user?.data?.sign.isNullOrEmpty()) {
                                            Text(
                                                text = user?.data?.sign ?: "",
                                                color = Color.White,
                                                modifier = Modifier
                                                    .alpha(0.8f)
                                                    .animateContentSize(
                                                        animationSpec = tween(
                                                            durationMillis = 300
                                                        )
                                                    )
                                                    .clickable {
                                                        maxLines =
                                                            if (maxLines == 1) Int.MAX_VALUE else 1
                                                    },
                                                maxLines = maxLines,
                                                fontFamily = puhuiFamily,
                                                fontSize = 8.5.sp,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Row(
                                            modifier = Modifier
                                                .clickVfx {
                                                    if (isSubscribed == true) viewModel.unfollowUser(
                                                        userMid
                                                    ) else viewModel.followUser(userMid)
                                                }
                                                .clip(
                                                    RoundedCornerShape(360.dp)
                                                )

                                                .background(followButtonColor)
                                                .padding(
                                                    start = 8.dp,
                                                    end = 11.dp,
                                                    top = 3.dp,
                                                    bottom = 5.dp
                                                )

                                        ) {
                                            var buttonTextHeight by remember {
                                                mutableStateOf(0.dp)
                                            }
                                            Crossfade(
                                                targetState = isSubscribed,
                                                animationSpec = tween(durationMillis = 400)
                                            ) {
                                                if (it == true) {
                                                    Icon(
                                                        imageVector = Icons.Default.Done,
                                                        contentDescription = null,
                                                        tint = Color.White,
                                                        modifier = Modifier.size(buttonTextHeight)
                                                    )
                                                } else {
                                                    Icon(
                                                        imageVector = Icons.Default.Add,
                                                        contentDescription = null,
                                                        tint = Color.White,
                                                        modifier = Modifier.size(buttonTextHeight)
                                                    )
                                                }
                                            }

                                            Spacer(modifier = Modifier.width(2.dp))
                                            Text(
                                                text = "${if (isSubscribed == true) "已关注" else "关注"}  ${(fans ?: 0).toShortChinese()}",
                                                color = Color.White,
                                                fontFamily = puhuiFamily,
                                                fontWeight = FontWeight.Medium,
                                                modifier = Modifier
                                                    .animateContentSize(
                                                        animationSpec = tween(
                                                            durationMillis = 400
                                                        )
                                                    )
                                                    .onGloballyPositioned {
                                                        buttonTextHeight = with(localDensity) {
                                                            it.size.height.toDp()
                                                        }
                                                    }, fontSize = 9.sp
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                            }
                        }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .border(
                                    width = 0.1.dp,
                                    color = Color(112, 112, 112, 70),
                                    shape = RoundedCornerShape(
                                        topStart = 8.dp,
                                        topEnd = 8.dp
                                    )
                                )
                                .background(Color(36, 36, 36, 100))
                        ) {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                var tabHeight by remember {
                                    mutableStateOf(0.dp)
                                }
                                var isSearchBoxExpand by remember {
                                    mutableStateOf(false)
                                }
                                Crossfade(targetState = isSearchBoxExpand && pagerState.currentPage == 0) {
                                    if (it) {
                                        BasicTextField(
                                            value = searchBoxKeyword,
                                            modifier = Modifier
                                                .padding(
                                                    start = 6.dp,
                                                    end = 4.dp.plus(tabHeight - 14.dp),
                                                    top = 8.dp,
                                                    bottom = 6.dp
                                                )
                                                .border(
                                                    width = (0.5).dp, color = Color(
                                                        255,
                                                        255,
                                                        255,
                                                        61
                                                    ), shape = RoundedCornerShape(360.dp)
                                                )
                                                .clip(RoundedCornerShape(360.dp))
                                                .fillMaxWidth()
                                                .background(Color.Transparent)
                                                .padding(
                                                    start = 12.dp,
                                                    end = 13.dp,
                                                    top = 3.dp,
                                                    bottom = 4.dp
                                                ),
                                            onValueChange = { value ->
                                                searchBoxKeyword = value
                                                viewModel.getVideos(userMid, true, value)
                                            },
                                            textStyle = TextStyle(
                                                color = Color.White,
                                                fontSize = 10.sp,
                                                fontFamily = puhuiFamily
                                            ),
                                            cursorBrush = SolidColor(
                                                BilibiliPink
                                            )
                                        )
                                    } else {
                                        LazyRow(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .verticalScroll(
                                                    rememberScrollState()
                                                )
                                                .onGloballyPositioned {
                                                    tabHeight =
                                                        with(localDensity) { it.size.height.toDp() }
                                                }, contentPadding = PaddingValues(
                                                start = 6.dp, end = 6.dp, top = 8.dp, bottom = 6.dp
                                            )
                                        ) {
                                            item {
                                                TabItem(
                                                    text = "投稿",
                                                    isSelected = pagerState.currentPage == 0
                                                ) {
                                                    scope.launch {
                                                        pagerState.animateScrollToPage(0)
                                                    }
                                                }
                                            }
                                            item {
                                                TabItem(
                                                    text = "动态",
                                                    isSelected = pagerState.currentPage == 1
                                                ) {
                                                    scope.launch {
                                                        pagerState.animateScrollToPage(1)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                Row(
                                    modifier = Modifier.align(
                                        Alignment.CenterEnd
                                    )
                                ) {
                                    AnimatedVisibility(
                                        visible = pagerState.currentPage == 0,
                                        enter = fadeIn(),
                                        exit = fadeOut()
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Search,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(tabHeight - 14.dp)
                                                .padding(end = 6.dp)
                                                .clickable {
                                                    isSearchBoxExpand = !isSearchBoxExpand
                                                }
                                        )
                                    }
                                }
                            }

                            HorizontalPager(count = 2, state = pagerState) { page ->
                                when (page) {
                                    0 -> {
                                        LazyColumn(
                                            modifier = Modifier
                                                .fillMaxSize(),
                                            contentPadding = PaddingValues(
                                                horizontal = 6.dp
                                            )
                                        ) {
                                            userVideos?.forEach {
                                                item(key = it.aid) {
                                                    VideoUis.VideoCard(
                                                        videoName = it.title,
                                                        uploader = it.author,
                                                        views = it.play.toShortChinese(),
                                                        coverUrl = it.pic,
                                                        badge = if (it.is_union_video == 1) "合作" else if (it.is_live_playback == 1) "直播回放" else if (it.is_pay == 1) "付费" else "",
                                                        videoBvid = it.bvid,
                                                        context = this@SpaceProfileActivity,
                                                        clickable = true,
                                                        modifier = Modifier.animateItemPlacement()
                                                    )
                                                }
                                            }
                                            item {
                                                LaunchedEffect(key1 = Unit, block = {
                                                    viewModel.getVideos(
                                                        userMid,
                                                        false,
                                                        searchBoxKeyword
                                                    )
                                                })
                                            }
                                        }
                                    }
                                    1 -> {
                                        LazyColumn(
                                            modifier = Modifier.fillMaxSize(),
                                            //contentPadding = PaddingValues(vertical = 4.dp)
                                        ) {
                                            dynamicList?.forEach { item ->
                                                item {
                                                    DynamicCardNew(
                                                        item = item,
                                                        context = this@SpaceProfileActivity
                                                    )
                                                }
                                            }
                                            if (!dynamicList.isNullOrEmpty()) {
                                                item {
                                                    LaunchedEffect(key1 = Unit) {
                                                        viewModel.getDynamic(false, userMid)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun TabItem(text: String, isSelected: Boolean, onClick: () -> Unit) {
        val color by animateColorAsState(
            targetValue = if (isSelected) BilibiliPink else Color.Transparent,
            animationSpec = tween(durationMillis = 300)
        )
        val borderWidth by animateDpAsState(
            targetValue = if (isSelected) 0.dp else 2.dp,
            animationSpec = tween(durationMillis = 300)
        )
        Row {
            Text(
                text = text,
                color = Color.White,
                fontFamily = puhuiFamily,
                modifier = Modifier
                    .border(
                        width = borderWidth, color = Color(
                            255,
                            255,
                            255,
                            61
                        ), shape = RoundedCornerShape(360.dp)
                    )
                    .clip(RoundedCornerShape(360.dp))
                    .background(color)
                    .padding(start = 12.dp, end = 13.dp, top = 3.dp, bottom = 4.dp)
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null,
                        onClick = { onClick() }),
                fontWeight = FontWeight.Medium, fontSize = 10.sp

            )
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}