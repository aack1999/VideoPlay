package com.aution.paidd.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.aution.paidd.R;

public class H5Activity extends AppCompatActivity {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);


        //getSupportActionBar().setTitle(getIntent().getStringExtra("title"));

        TextView money = (TextView) findViewById(R.id.money);

        money.setText("租赁只需要"+getIntent().getStringExtra("money")+"哦!");

        WebView webView=(WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.loadUrl(getIntent().getStringExtra("url"));

        webView.setWebViewClient(new WebViewClient());


    }

    public void  btn(View v){
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+getIntent().getStringExtra("phone")));
        startActivity(intent);
    }



}
