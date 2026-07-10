package ru.spb.skynet.lk.components.main.home

import android.app.Activity
import android.widget.Space
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.launch
import ru.spb.skynet.lk.R
import ru.spb.skynet.lk.components.balance.BalanceBottomSheet
import ru.spb.skynet.lk.data.network.NetworkState
import ru.spb.skynet.lk.extensions.shimmer
import ru.spb.skynet.lk.ui.theme.SkynetGreen
import ru.spb.skynet.lk.ui.theme.SkynetYellow
import ru.spb.skynet.lk.viewModels.home.HomeViewModel
import java.util.Locale
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel) {
    val view = LocalView.current
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    val abonent by viewModel.abonent.collectAsStateWithLifecycle()
    val networkState by viewModel.networkState.collectAsStateWithLifecycle()
    val pos by viewModel.pos.collectAsStateWithLifecycle()

    val pullToRefreshState = rememberPullToRefreshState()
    val isRefreshing = networkState is NetworkState.Loading
    var isHidden by remember { mutableStateOf(false) }

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        if (abonent == null && pos == null) {
            viewModel.abonent()
        }
    }

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = SkynetGreen.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    val headerHeightDp = (configuration.screenHeightDp * 0.85f).dp
    val maxSheetOffsetDp = headerHeightDp / 2.2f

    val statusBarHeightPx = WindowInsets.statusBars.asPaddingValues(density).calculateTopPadding().value * density.density
    val headerHeightPx = headerHeightDp.value * density.density
    val maxSheetOffsetPx = maxSheetOffsetDp.value * density.density

    var sheetOffset by remember { mutableStateOf(maxSheetOffsetPx) }

    val nestedScrollConnection = remember(headerHeightPx, maxSheetOffsetPx, statusBarHeightPx) {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y

                if (delta < 0 && sheetOffset > statusBarHeightPx) {
                    val newOffset = (sheetOffset + delta).coerceIn(statusBarHeightPx, maxSheetOffsetPx)
                    val consumed = newOffset - sheetOffset
                    sheetOffset = newOffset
                    return Offset(0f, consumed)
                }

                if (delta > 0 && sheetOffset < maxSheetOffsetPx) {
                    val newOffset = (sheetOffset + delta).coerceIn(statusBarHeightPx, maxSheetOffsetPx)
                    val consumed = newOffset - sheetOffset
                    sheetOffset = newOffset
                    return Offset(0f, consumed)
                }

                return Offset.Zero
            }
        }
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = { viewModel.abonent },
        state = pullToRefreshState,
        modifier = Modifier.fillMaxSize(),
        indicator = {
            PullToRefreshDefaults.LoadingIndicator(
                state = pullToRefreshState,
                isRefreshing = isRefreshing,
                modifier = Modifier.align(Alignment.TopCenter),
                color = SkynetYellow,
                containerColor = Color.Transparent
            )
        }
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(SkynetGreen)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(headerHeightDp)
                    .statusBarsPadding()
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(1f).padding(start = 20.dp, end = 20.dp).shimmer(12.dp, isLoading = networkState is NetworkState.Loading),
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        text = "${pos?.pos?.first()?.address} ,${ pos?.pos?.first()?.locationFlat}, ${"этаж."} ${ pos?.pos?.first()?.locationFloor}"
                    )
                    IconButton(
                        modifier = Modifier.shimmer(12.dp, networkState is NetworkState.Loading),
                        onClick = {
                            navController.navigate("chat")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.SupportAgent,
                            tint = Color.White,
                            contentDescription = null
                        )
                    }
                    Spacer(modifier = Modifier.width(1.dp))
                    IconButton(
                        modifier = Modifier.shimmer(12.dp, networkState is NetworkState.Loading),
                        onClick = { navController.navigate("notifications") }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(25.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                }

                Text(
                    text = if (networkState is NetworkState.Loading) { "" } else { "${"Счет № ${abonent?.info?.userId}"} " },
                    color = Color.White,
                    modifier = Modifier
                        .shimmer(12.dp, networkState is NetworkState.Loading)
                        .border(1.dp, Color.White.copy(alpha = 0.5f), CircleShape)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.balance),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.shimmer(12.dp, networkState is NetworkState.Loading)
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.wrapContentSize(),
                    verticalAlignment = Alignment.CenterVertically) {

                    val moneyAmount = abonent?.info?.money?.toDoubleOrNull() ?: 0.0
                    val formattedMoney = String.format(Locale.getDefault(), "%.2f", moneyAmount)
                    val displayText = if (!isHidden) {
                        "$formattedMoney₽"
                    } else {
                        "••••₽"
                    }

                    Text(
                        text = displayText,
                        modifier = Modifier.shimmer(12.dp, networkState is NetworkState.Loading),
                        fontWeight = FontWeight.Bold,
                        fontSize = 34.sp,
                        color = Color.White
                    )

                    if (networkState != NetworkState.Loading) {
                        Spacer(modifier = Modifier.width(20.dp))
                    } else {
                        Spacer(modifier = Modifier.width(30.dp))
                    }

                    IconToggleButton(
                        modifier = Modifier.size(24.dp).shimmer(12.dp, networkState is NetworkState.Loading),
                        checked = isHidden,
                        onCheckedChange = {
                            isHidden = it
                        },
                    ) {
                        Icon(
                            imageVector = if (isHidden) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }

                Row(
                    modifier = Modifier.wrapContentSize().shimmer(12.dp, isLoading = networkState is NetworkState.Loading),
                    horizontalArrangement = Arrangement.spacedBy(
                        12.dp,
                        Alignment.CenterHorizontally
                    )
                ) {
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier.size(56.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_promised),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp),
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                    }

                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier.size(56.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_card),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                    }

                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier.size(56.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_autopay),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(5.dp))
                Button(
                    onClick = {
                        showBottomSheet = true
                    },
                    modifier = Modifier.shimmer(12.dp, isLoading = networkState is NetworkState.Loading),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SkynetYellow)
                ) {
                    Text(
                        text = stringResource(R.string.top_up_balance),
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }

            val strokeProgress = if (maxSheetOffsetPx > 0f) {
                ((maxSheetOffsetPx - sheetOffset) / maxSheetOffsetPx).coerceIn(0f, 1f)
            } else 0f

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(configuration.screenHeightDp.dp)
                    .offset { IntOffset(0, sheetOffset.roundToInt()) }
                    .background(Color.White, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .zIndex(1f)
                    .nestedScroll(nestedScrollConnection)
                    .draggable(
                        orientation = Orientation.Vertical,
                        state = rememberDraggableState { delta ->
                            val newOffset = sheetOffset + delta
                            sheetOffset = newOffset.coerceIn(statusBarHeightPx, maxSheetOffsetPx)
                        }
                    )
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top) {
                Canvas(modifier = Modifier.size(40.dp, 12.dp)) {
                    val width = size.width
                    val height = size.height
                    val centerY = height / 2
                    val arrowYOffset = centerY + (10.dp.toPx() * strokeProgress)
                    val path = Path().apply {
                        moveTo(0f, centerY)
                        lineTo(width / 2, arrowYOffset)
                        lineTo(width, centerY)
                    }
                    drawPath(
                        path = path,
                        color = Color.LightGray.copy(alpha = 0.5f + (0.5f * strokeProgress)),
                        style = Stroke(
                            width = 4.dp.toPx(),
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round
                        )
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Ваши услуги",
                    modifier = Modifier.shimmer(12.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(5) { index ->
                        Text(
                            text = "Услуга ${index + 1}",
                            modifier = Modifier.fillMaxWidth().shimmer(12.dp).background(
                                Color.LightGray.copy(alpha = 0.4f),
                                RoundedCornerShape(8.dp)
                            ).padding(12.dp),
                            color = Color.Black
                        )
                    }
                    item { Spacer(modifier = Modifier.height(32.dp)) }
                }
            }
        }
    }
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },

            sheetState = sheetState,
            containerColor = Color.White,
            dragHandle = {}
        ) {

            BalanceBottomSheet(
                onClose = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showBottomSheet = false
                            navController.navigate("payments_history")
                        }
                    }
                },
                onNavigate = { route ->
                    scope.launch {
                        sheetState.hide()
                    }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showBottomSheet = false
                            navController.navigate(route)
                        }
                    }
                }
            )
        }
    }
}
