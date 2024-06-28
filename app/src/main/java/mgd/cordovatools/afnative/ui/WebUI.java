package mgd.cordovatools.afnative.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;

import org.apache.cordova.CordovaActivity;
import org.apache.cordova.engine.SystemWebView;

import mgd.cordovatools.afnative.R;

public class WebUI extends CordovaActivity {

    public static void openURL(Context mContext, String mLink)
    {
        Intent cdvIntent = new Intent(mContext, WebUI.class);
        cdvIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        cdvIntent.putExtra("mLink", mLink);
        mContext.startActivity(cdvIntent);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String url = getIntent().getStringExtra("mLink");
        //loadUrl(url);

        setContentView(R.layout.fragment_frag_policy_u_i);
        SystemWebView webView = findViewById(R.id.cdvWebView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.loadUrl(url);
    }

}
