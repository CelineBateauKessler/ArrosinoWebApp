package com.celinekessler.arrosino;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    private WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Webview
        myWebView = (WebView) findViewById(R.id.webview);
        myWebView.setWebViewClient(new MyWebViewClient());
        /*.setWebChromeClient(new WebChromeClient() {
            public void onGeolocationPermissionsShowPrompt(String origin, android.webkit.GeolocationPermissions.Callback callback) {
                // callback.invoke(String origin, boolean allow, boolean remember);
                callback.invoke(origin, true, false);
            }
        });*/
        myWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        //myWebView.getSettings().setGeolocationEnabled(true);
        myWebView.getSettings().setJavaScriptEnabled(true);

        //myWebView.addJavascriptInterface(new WebAppInterface(this), "Android");

        myWebView.loadUrl("http://arrosino.duckdns.org:8080/sd/arrosino");

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
            myWebView.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i("WEBVIEW", Uri.parse(url).getHost());
            if (Uri.parse(url).getHost().equals("arrosino.duckdns.org")) {
                // This is my web site, so do not override; let my WebView load the page
                return false;
            }
            // Pour être certain de ne pas ouvrir un autre site (par exemple défini par un lien dans un commentaire)
            // DANS MA WebView qui réagit avec MA WebAppInterface
            // On lance un autre intent pour gérer ce site
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }
    }
}
