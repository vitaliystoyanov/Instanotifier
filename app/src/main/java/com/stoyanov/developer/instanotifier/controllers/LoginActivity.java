package com.stoyanov.developer.instanotifier.controllers;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.joanzapata.android.asyncservice.api.annotation.InjectService;
import com.joanzapata.android.asyncservice.api.annotation.OnMessage;
import com.joanzapata.android.asyncservice.api.internal.AsyncService;
import com.stoyanov.developer.instanotifier.R;
import com.stoyanov.developer.instanotifier.model.Configuration;
import com.stoyanov.developer.instanotifier.model.events.FinishLoginActivityEvent;
import com.stoyanov.developer.instanotifier.model.services.Service;

import de.greenrobot.event.EventBus;
import me.zhanghai.android.materialprogressbar.IndeterminateProgressDrawable;


public class LoginActivity extends Activity {

    private static final String TAG = "DBG";

    private String colorString = "#2d5b81";
    private WebView webView;
    private ProgressBar progressBar;

    @InjectService
    public Service service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        if (progressBar != null) {
            IndeterminateProgressDrawable drawable = new IndeterminateProgressDrawable(this);
            drawable.setColorFilter(Color.parseColor(colorString), PorterDuff.Mode.SRC);
            progressBar.setIndeterminateDrawable(drawable);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
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
            Log.d(TAG, "Redirecting URL " + url);
            if (url.startsWith(Configuration.REDIRECT_URL)) {
                String urls[] = url.split("=");
                String accesstToken = urls[1];

                service.auth(accesstToken);
                return true;
            }
            return false;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            Log.d(TAG, "Page error: " + description);

            Toast.makeText(getApplicationContext()
                    , getResources().getString(R.string.not_internet_connection)
                    , Toast.LENGTH_SHORT).show();
            finish();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.d(TAG, "Loading URL: " + url);
            progressBar.setVisibility(View.VISIBLE);
            if (pendingUrl == null) pendingUrl = url;
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.INVISIBLE);
            if (!url.equals(pendingUrl)) {
                Log.d("DBG", "Detected HTTP redirect " + pendingUrl + "->" + url);
                pendingUrl = null;
            }
            Log.d(TAG, "onPageFinished URL: " + url);
        }
    }

    @OnMessage(FinishLoginActivityEvent.class)
    public void onEvent(FinishLoginActivityEvent event) {
        Log.i("DBG","onEvent(FinishLoginActivityEvent event)");
        finish();
    }

    @Override
    protected void onDestroy() {
        AsyncService.unregister(this);
        super.onDestroy();
    }
}
