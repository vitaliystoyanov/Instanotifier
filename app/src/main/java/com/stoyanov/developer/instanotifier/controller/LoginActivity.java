package com.stoyanov.developer.instanotifier.controller;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.joanzapata.android.asyncservice.api.annotation.InjectService;
import com.joanzapata.android.asyncservice.api.annotation.OnMessage;
import com.joanzapata.android.asyncservice.api.internal.AsyncService;
import com.stoyanov.developer.instanotifier.R;
import com.stoyanov.developer.instanotifier.model.Configuration;
import com.stoyanov.developer.instanotifier.model.serviceevents.FinishLoginEvent;
import com.stoyanov.developer.instanotifier.model.services.AuthorizationService;

import de.greenrobot.event.EventBus;

public class LoginActivity extends Activity {

    private static final String TAG = "DBG";
    private CircularProgressView progressView;
    private WebView webView;

    @InjectService
    public AuthorizationService authorizationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressView = (CircularProgressView) findViewById(R.id.progress_view);
        progressView.startAnimation();
        int colorProgressBar = getResources().getColor(R.color.primary_second_color);
        progressView.setColor(colorProgressBar);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        AsyncService.inject(this);
        CookieSyncManager.createInstance(getApplication());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();

        webView = (WebView) findViewById(R.id.webView);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setWebViewClient(new OAuthWebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(Configuration.AUTH_URL);
    }

    private class OAuthWebViewClient extends WebViewClient {

        private String pendingUrl;

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, "[LoginActivity]Redirecting URL " + url);
            if (url.startsWith(Configuration.REDIRECT_URL)) {
                String urls[] = url.split("=");
                String accesstToken = urls[1];
                authorizationService.authorization(accesstToken);
                return true;
            }
            return false;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            Log.d(TAG, "[LoginActivity]Page error: " + description);
            Toast.makeText(getApplicationContext()
                    , getResources().getString(R.string.not_internet_connection)
                    , Toast.LENGTH_SHORT).show();
            finish();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progressView.setVisibility(View.VISIBLE);
            Log.d(TAG, "[LoginActivity]Loading URL: " + url);
            if (pendingUrl == null) pendingUrl = url;
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressView.setVisibility(View.INVISIBLE);
            if (!url.equals(pendingUrl)) {
                Log.d("DBG", "[LoginActivity]Detected HTTP redirect " + pendingUrl + "->" + url);
                pendingUrl = null;
            }
            Log.d(TAG, "[LoginActivity]onPageFinished URL: " + url);
        }
    }

    @OnMessage(FinishLoginEvent.class)
    public void onEvent(FinishLoginEvent event) {
        finish();
    }

    @Override
    protected void onDestroy() {
        AsyncService.unregister(this);
        super.onDestroy();
    }
}
