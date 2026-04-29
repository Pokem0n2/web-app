package com.ces.viewer;

import android.os.Bundle;
import android.view.WindowManager;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings;
import android.webkit.WebChromeClient;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private static final String TARGET_URL = "http://47.103.78.78:1118";
    private static final String TAG = "MainActivity";
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Use edge-to-edge with status bar visible
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        // Create a container layout
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        setContentView(container);

        // Create black spacer for status bar area
        View statusBarSpacer = new View(this);
        LinearLayout.LayoutParams spacerParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            0
        );
        spacerParams.weight = 0;
        // Get status bar height
        int statusBarHeight = getStatusBarHeight();
        spacerParams.height = statusBarHeight;
        statusBarSpacer.setLayoutParams(spacerParams);
        statusBarSpacer.setBackgroundColor(android.graphics.Color.BLACK);
        container.addView(statusBarSpacer);

        // Create WebView
        webView = new WebView(this);
        LinearLayout.LayoutParams webViewParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            0,
            1.0f
        );
        webView.setLayoutParams(webViewParams);

        // Configure WebView
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setAllowFileAccess(false);
        settings.setAllowContentAccess(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUserAgentString(
            "Mozilla/5.0 (Linux; Android 14; Pixel 7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36"
        );

        // Set WebViewClient to keep navigation in WebView
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d(TAG, "Loading URL: " + url);
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    return false;
                }
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
                Log.d(TAG, "Page started: " + url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d(TAG, "Page finished: " + url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.e(TAG, "WebView error: " + errorCode + " - " + description);
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                Log.d(TAG, "Progress: " + newProgress + "%");
            }
        });

        container.addView(webView);

        // Load target URL
        Log.d(TAG, "Loading target: " + TARGET_URL);
        webView.loadUrl(TARGET_URL);

        // Keep screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        Log.d(TAG, "Status bar height: " + result);
        return result;
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.destroy();
        }
        super.onDestroy();
    }
}
