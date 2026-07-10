package ru.spb.skynet.lk.components.balance.checks

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ru.spb.skynet.lk.R
import ru.spb.skynet.lk.viewModels.balance.BalanceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun CashReceiptScreen(navController: NavController, viewModel: BalanceViewModel) {

    val receiptUrl = "${ viewModel.receiptUrl }${ viewModel.checkId }"
    val sessionToken by viewModel.sessionToken.collectAsStateWithLifecycle()

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
                            stringResource(R.string.cash_receipt),
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
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    WebView(context).apply {
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true

                        webViewClient = object : WebViewClient() {
                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)

                                val jsCode = """
    (function() {
        var iframes = document.getElementsByTagName('iframe');
        if (iframes.length > 0) {
            // 1. Очищаем дефолтные стили страницы SkyNet
            document.body.style.margin = '0';
            document.body.style.padding = '0';
            document.body.style.background = '#FFFFFF';
            document.body.style.overflow = 'hidden'; // прячем прокрутку родительского окна
            
            // Скрываем все остальные элементы ЛК
            for (var i = 0; i < document.body.children.length; i++) {
                document.body.children[i].style.display = 'none';
            }
            
            var targetIframe = iframes[0];
            
            // 2. Рассчитываем масштаб, чтобы чек влез по ширине экрана смартфона
            var phoneWidth = window.innerWidth || document.documentElement.clientWidth;
            
            // Задаем базовую ширину для чека (например, 800px, если он сверстан под ПК).
            // Можете поменять 800 на 600 или 1000 в зависимости от реального размера чека SkyNet.
            var contentWidth = 800; 
            var scaleFactor = phoneWidth / contentWidth;
            
            // Если экран телефона меньше ширины чека, уменьшаем его масштаб
            if (scaleFactor < 1) {
                targetIframe.style.width = contentWidth + 'px';
                targetIframe.style.height = (100 / scaleFactor) + '%';
                targetIframe.style.transform = 'scale(' + scaleFactor + ')';
                targetIframe.style.transformOrigin = 'top left'; // масштабируем от верхнего левого угла
            } else {
                targetIframe.style.width = '100%';
                targetIframe.style.height = '100%';
            }
            
            // 3. Позиционируем iframe на весь экран
            targetIframe.style.display = 'block';
            targetIframe.style.position = 'fixed';
            targetIframe.style.top = '0';
            targetIframe.style.left = '0';
            targetIframe.style.border = 'none';
            targetIframe.style.zIndex = '999999';
            
            document.body.appendChild(targetIframe);
        }
    })();
""".trimIndent()
                                view?.evaluateJavascript(jsCode, null)
                            }
                        }
                        val headers = hashMapOf<String, String>().apply {
                            put("Cookie", sessionToken ?: "")
                        }
                        loadUrl(receiptUrl, headers)
                    }
                }
            )
        }
    }
}