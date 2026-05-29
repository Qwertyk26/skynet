package ru.spb.skynet.lk.components.progress_bar

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.google.android.material.loadingindicator.LoadingIndicator
import ru.spb.skynet.lk.ui.theme.SkynetGreen

@Composable
fun ProgressBar() {
    BackHandler(enabled = true) { }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.7f)),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(56.dp),
            color = SkynetGreen,
            strokeWidth = 6.dp,
            trackColor = SkynetGreen.copy(alpha = 0.7f),
            strokeCap = StrokeCap.Round
        )
    }
}