package com.ces.viewer;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {
    private static final String TARGET_URL = "http://47.103.78.78:1118";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configure WebView to load external URLs directly
        WebView webView = this.getBridge().getWebView();
        if (webView != null) {
            // Enable JavaScript
            webView.getSettings().setJavaScriptEnabled(true);

            // Allow mixed content (HTTP on HTTPS page)
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

            // Disable file access within WebView
            webView.getSettings().setAllowFileAccess(false);

            // Disable Content URL access for security
            webView.getSettings().setAllowContentAccess(false);

            // Set mobile Chrome User-Agent
            String mobileUA = "Mozilla/5.0 (Linux; Android 14; Pixel 7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36";
            webView.getSettings().setUserAgentString(mobileUA);

            // Load all URLs in this WebView, not browser
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    // Load all http/https URLs in WebView
                    if (url.startsWith("http://") || url.startsWith("https://")) {
                        view.loadUrl(url);
                        return true;
                    }
                    return false;
                }
            });

            // Directly load the target URL
            webView.loadUrl(TARGET_URL);
        }

        // Enable edge-to-edge display
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        // Hide system bars (status bar + navigation bar)
        WindowInsetsControllerCompat controller = WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        if (controller != null) {
            controller.hide(WindowInsetsCompat.Type.systemBars());
            controller.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        }

        // Keep screen on while app is running
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            // Re-hide system bars when focus returns
            WindowInsetsControllerCompat controller = WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
            if (controller != null) {
                controller.hide(WindowInsetsCompat.Type.systemBars());
                controller.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            }
        }
    }
}
