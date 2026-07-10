package ru.spb.skynet.lk.components.main.connect

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ru.spb.skynet.lk.R
import ru.spb.skynet.lk.data.network.NetworkState
import ru.spb.skynet.lk.extensions.shimmer
import ru.spb.skynet.lk.ui.theme.Background
import ru.spb.skynet.lk.ui.theme.SkynetGreen
import ru.spb.skynet.lk.viewModels.home.HomeViewModel

@Composable
fun ConnectScreen(navController: NavController, viewModel: HomeViewModel) {

    val networkState by viewModel.networkState.collectAsStateWithLifecycle()
    val pos by viewModel.pos.collectAsStateWithLifecycle()

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }
    val itemsList = List(20) { "Item#$it" }
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Background)) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(start = 20.dp, end = 20.dp)

        ) {
            Text(
                modifier = Modifier.padding(top = 15.dp)
                    .shimmer(12.dp, isLoading = networkState is NetworkState.Loading),
                color = SkynetGreen,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                text = "${pos?.pos?.first()?.address} ,${pos?.pos?.first()?.locationFlat}, ${"этаж."} ${pos?.pos?.first()?.locationFloor}"
            )

            Text(
                modifier = Modifier.padding(top = 40.dp),
                text = stringResource(R.string.services),
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = stringResource(R.string.services_subtitle),
                color = Color.Gray
            )
            LazyVerticalGrid(
                modifier = Modifier.padding(top = 10.dp),
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(itemsList) { item ->
                    ElevatedCard(
                        modifier = Modifier.height(200.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 0.dp,
                            pressedElevation = 0.dp,
                            focusedElevation = 0.dp
                        ),
                        onClick = {

                        },
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {
                        Column(modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally) {

                            Surface(
                                modifier = Modifier.size(48.dp),
                                shape = CircleShape,
                                color = SkynetGreen
                            ) {
                                Icon(
                                    modifier = Modifier.size(24.dp)
                                        .scale(0.5f),
                                    imageVector = Icons.Default.Wifi,
                                    tint = Color.White,
                                    contentDescription = null
                                )
                            }
                            Text(
                                modifier = Modifier.padding(10.dp),
                                fontWeight = FontWeight.Bold,
                                text = item
                            )
                        }
                    }
                }
            }
        }
    }
}