package ru.spb.skynet.lk.components.bank_card

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.valentinilk.shimmer.shimmer
import ru.spb.skynet.lk.R
import ru.spb.skynet.lk.data.network.NetworkState
import ru.spb.skynet.lk.extensions.shimmer
import ru.spb.skynet.lk.ui.theme.SkynetGreen
import ru.spb.skynet.lk.viewModels.bank_card.BankCardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BankCardScreen(navController: NavController, viewModel: BankCardViewModel) {

    val tokensData by viewModel.tokens.collectAsStateWithLifecycle()
    val networkState by viewModel.networkState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        if (tokensData?.data.isNullOrEmpty()) {
            viewModel.card()
        }
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            Column {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
                    title = {
                        Text(
                            stringResource(R.string.top_up_card),
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null
                            )
                        }
                    }
                )
                HorizontalDivider(
                    thickness = 0.5.dp,
                    color = Color.LightGray.copy(alpha = 0.5f)
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
        ) {
            when (networkState) {
                is NetworkState.Error -> {
                    Text(
                        text = stringResource(R.string.empty),
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Gray
                    )
                }

                is NetworkState.Loading, is NetworkState.Success -> {
                    val isLoading = networkState is NetworkState.Loading

                    val itemsList = if (isLoading) {
                        List(7) { null }
                    } else {
                        Log.d("Tokens", "${ tokensData?.data }")
                        tokensData?.data ?: emptyList()
                    }

                    if (!isLoading && itemsList.isEmpty()) {
                        Text(
                            text = stringResource(R.string.empty),
                            modifier = Modifier.align(Alignment.Center),
                            color = Color.Gray
                        )
                    } else {
                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            LazyColumn(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                            ) {
                                items(items = itemsList) { token ->
                                    ListItem(
                                        colors = ListItemDefaults.colors(containerColor = Color.White),
                                        headlineContent = {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    modifier = Modifier.shimmer(12.dp, isLoading),
                                                    painter = painterResource(R.drawable.outline_credit_card_24),
                                                    tint = SkynetGreen,
                                                    contentDescription = null
                                                )

                                                Column(
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .padding(start = 20.dp, end = 12.dp)
                                                ) {
                                                    Text(
                                                        text = if (isLoading) "00.00.0000 00:00" else token?.pan ?: "",
                                                        color = if (isLoading) Color.Transparent else Color.Black,
                                                        maxLines = 1,
                                                        overflow = TextOverflow.Ellipsis,
                                                        modifier = Modifier.shimmer(12.dp, isLoading = isLoading)
                                                    )
                                                    if (isLoading) {
                                                        Spacer(modifier = Modifier.height(10.dp))
                                                    }
                                                    Text(
                                                        text = if (isLoading) "0000 " else token?.bank ?: "",
                                                        color = if (isLoading) Color.Transparent else Color.Gray,
                                                        maxLines = 1,
                                                        overflow = TextOverflow.Ellipsis,
                                                        modifier = Modifier.shimmer(12.dp, isLoading = isLoading)
                                                    )
                                                }

                                                Column(
                                                    horizontalAlignment = Alignment.End,
                                                    modifier = Modifier.fillMaxHeight()
                                                ) {
                                                    Text(
                                                        text = "Автоплатеж",
                                                        fontSize = 12.sp,
                                                        color = if (isLoading) Color.Transparent else Color.Gray,
                                                        modifier = Modifier.shimmer(12.dp, isLoading = isLoading)
                                                    )


                                                    Switch(
                                                        modifier = Modifier.shimmer(12.dp, isLoading = isLoading).scale(0.75f).requiredHeight(24.dp),
                                                        checked = token?.autopay ?: false,
                                                        enabled = !isLoading,
                                                        onCheckedChange = { isChecked ->

                                                        },
                                                        colors = SwitchDefaults.colors(
                                                            checkedBorderColor = SkynetGreen,
                                                            checkedThumbColor = SkynetGreen,
                                                            checkedTrackColor = Color.White,
                                                        ),
                                                    )
                                                }
                                            }
                                        }
                                    )
                                    HorizontalDivider(
                                        modifier = Modifier.padding(horizontal = 16.dp).shimmer(12.dp, isLoading = isLoading),
                                        thickness = 0.5.dp,
                                        color = Color.LightGray.copy(alpha = 0.3f)
                                    )
                                }
                            }
                            Button(
                                onClick = { /* Действие при нажатии */ },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .shimmer(12.dp, isLoading = isLoading)
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SkynetGreen),
                                enabled = !isLoading
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),

                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.outline_credit_card_24),
                                        tint = Color.White,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(24.dp)
                                            .align(Alignment.CenterStart)
                                    )
                                    Text(
                                        text = stringResource(R.string.new_card),
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                            }
                        }
                    }
                }
                else -> Unit
            }

        }
    }
}