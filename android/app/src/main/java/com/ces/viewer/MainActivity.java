package com.ces.viewer;

import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings;
import android.webkit.WebChromeClient;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {
    private static final String TARGET_URL = "http://47.103.78.78:1118";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configure WebView BEFORE super.onCreate triggers initial load
        // We do this AFTER super() because Bridge must be initialized first
        final WebView webView = this.getBridge().getWebView();

        if (webView != null) {
            WebSettings settings = webView.getSettings();

            // Essential settings
            settings.setJavaScriptEnabled(true);
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            settings.setDomStorageEnabled(true);
            settings.setDatabaseEnabled(true);

            // Security
            settings.setAllowFileAccess(false);
            settings.setAllowContentAccess(false);

            // Mobile User-Agent
            settings.setUserAgentString(
                "Mozilla/5.0 (Linux; Android 14; Pixel 7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36"
            );

            // Caching
            settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

            // Viewport
            settings.setUseWideViewPort(true);
            settings.setLoadWithOverviewMode(true);

            // CRITICAL: Prevent any URL from opening in external browser
            // Must be set BEFORE any page load
            webView.setWebViewClient(new WebViewClient() {
                private boolean initialLoadDone = false;

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    android.util.Log.d("MainActivity", "shouldOverrideUrlLoading: " + url);

                    // Block FIRST load (Capacitor's local server) and redirect to target
                    if (!initialLoadDone) {
                        initialLoadDone = true;
                        android.util.Log.d("MainActivity", "Blocking Capacitor local server, redirecting to: " + TARGET_URL);
                        view.loadUrl(TARGET_URL);
                        return true;
                    }

                    // After initial redirect, only block non-http(s) schemes
                    // http/https MUST return false to stay in WebView
                    if (url.startsWith("http://") || url.startsWith("https://")) {
                        return false; // false = let WebView handle it (stays in app)
                    }

                    // Block tel:, geo:, etc. from opening external apps
                    return true; // true = override, don't let system handle
                }

                @Override
                public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
                    android.util.Log.d("MainActivity", "onPageStarted: " + url);
                    super.onPageStarted(view, url, favicon);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    android.util.Log.d("MainActivity", "onPageFinished: " + url);
                    super.onPageFinished(view, url);
                }

                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    android.util.Log.e("MainActivity", "ERROR " + errorCode + ": " + description + " (" + failingUrl + ")");
                    super.onReceivedError(view, errorCode, description, failingUrl);
                }
            });

            webView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    android.util.Log.d("MainActivity", "Progress: " + newProgress + "%");
                }
            });
        }

        // Hide system bars for immersive fullscreen
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        WindowInsetsControllerCompat controller = WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        if (controller != null) {
            controller.hide(WindowInsetsCompat.Type.systemBars());
            controller.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            WindowInsetsControllerCompat controller = WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
            if (controller != null) {
                controller.hide(WindowInsetsCompat.Type.systemBars());
                controller.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            }
        }
    }
}
