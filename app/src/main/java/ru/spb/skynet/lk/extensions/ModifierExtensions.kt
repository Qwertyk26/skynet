package ru.spb.skynet.lk.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.valentinilk.shimmer.shimmer

@Composable
fun Modifier.shimmerIf(condition: Boolean): Modifier {
    return if (condition) this.shimmer() else this
}