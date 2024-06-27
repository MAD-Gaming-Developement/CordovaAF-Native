package mgd.cordovatools.afnative.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.apache.cordova.CordovaActivity;

public class WebUI extends CordovaActivity {

    public static void openURL(Context mContext, String mLink)
    {
        Intent cdvIntent = new Intent(mContext, WebUI.class);
        cdvIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        cdvIntent.putExtra("mLink", mLink);
        mContext.startActivity(cdvIntent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String url = getIntent().getStringExtra("mLink");
        loadUrl(url);
    }

}
