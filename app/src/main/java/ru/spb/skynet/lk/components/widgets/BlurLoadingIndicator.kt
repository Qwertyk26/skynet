package ru.spb.skynet.lk.components.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BlurLoadingIndicator(
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(88.dp), // Общий размер контейнера
        contentAlignment = Alignment.Center
    ) {
        // СЛОЙ 1: Эффект размытого стекла (Blur)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape) // Обрезаем размытие строго по форме круга
                // Полупрозрачный белый цвет создает эффект матового стекла
                .background(color.copy(alpha = 0.4f))
                // Задаем силу размытия заднего фона (оптимально от 10dp до 16dp)
                .blur(12.dp)
                // Тонкая полупрозрачная рамка подчеркнет геометрию "стекла"
                .border(0.5.dp, Color.White.copy(alpha = 0.6f), CircleShape)
        )

        // СЛОЙ 2: Сам экспрессивный индикатор (он остается четким)
        LoadingIndicator(
            modifier = Modifier.size(56.dp),
            color = color
        )
    }
}