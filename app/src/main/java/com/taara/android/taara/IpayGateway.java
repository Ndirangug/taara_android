package com.taara.android.taara;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class IpayGateway extends AppCompatActivity {

    String itemRfIds;
    String email;
    String total;
    String phone;
    Intent intent;
    String url;
    String storeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipay_gateway);

        intent = getIntent();
        email = intent.getStringExtra("EMAIL");
        phone = intent.getStringExtra("PHONE");
        itemRfIds = intent.getStringExtra("RFIDS");
        storeId = intent.getStringExtra("STORE_ID");
        total = String.valueOf(intent.getDoubleExtra("TOTAL", 0));


        url = getResources().getString(R.string.host) + "/taaraBackend/?android_api_call=checkout&itemIds=" + itemRfIds + "&amount=" + total + "&phone=" + phone + "&email=" + email;
        WebView webView = findViewById(R.id.ipayGateway);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadsImagesAutomatically(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(url);


    }
}
