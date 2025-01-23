package com.theikchan.screencloudwebplaybridge.ui.component

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebView(
    url: String,
    modifier: Modifier = Modifier,
    onWebViewCreated: (WebView) -> Unit,
    onWebViewLoaded: (isLoaded: Boolean, url: String) -> Unit
) {
    AndroidView(
        modifier = modifier,
        factory = {
            WebView(it).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                settings.javaScriptEnabled = true

                webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        Log.d("[WebView]", "Page started loading: $url")
                        super.onPageStarted(view, url, favicon)
                    }

                    override fun onPageFinished(view: WebView?, loadedUrl: String?) {
                        Log.d("[WebView]", "Page finished loading: $loadedUrl")
                        onWebViewLoaded(true, loadedUrl ?: "")
                        super.onPageFinished(view, url)
                    }
                }

                loadUrl(url)
                onWebViewCreated(this)
            }
        }, update = {
            it.loadUrl(url)
        })
}

