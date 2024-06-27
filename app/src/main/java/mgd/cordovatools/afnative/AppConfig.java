package mgd.cordovatools.afnative;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.appsflyer.attribution.AppsFlyerRequestListener;

import java.util.Map;
import java.util.Objects;

public class AppConfig extends Application {

    public static final String LOG_TAG = "CDV.AF";

    private static AppConfig _instance;
    private boolean isAFInitialized = false;
    private boolean isAFStarted = false;

    @Override
    public void onCreate() {
        super.onCreate();
        _instance = this;
        initAF("CCJiCSsjkBMbkhnXbazJQe");
    }

    public static AppConfig getInstance()
    {
        return _instance;
    }

    public synchronized void initAF(String appsFlyerID)
    {
        if(!isAFInitialized && isNetworkConnected())
        {
            AppsFlyerConversionListener conversionListener = new AppsFlyerConversionListener() {
                @Override
                public void onConversionDataSuccess(Map<String, Object> map) {
                    for (String attrName : map.keySet()) {
                        Log.d(LOG_TAG, "Conversion Success: " + attrName + "\nData Map:" + map.get(attrName));
                        String status = Objects.requireNonNull(map.get("af_status")).toString();
                        if (status.equals("Organic"))
                            Log.d(LOG_TAG, "Organic Install");
                        else
                            Log.d(LOG_TAG, "Non-Organic Install");
                    }
                }

                @Override
                public void onConversionDataFail(String s) {
                    Log.e(LOG_TAG, "Error Conversion of Event: " + s);
                }

                @Override
                public void onAppOpenAttribution(Map<String, String> map) {
                    Log.d(LOG_TAG, "First Open Attribution event)");
                }

                @Override
                public void onAttributionFailure(String s) {
                    Log.e(LOG_TAG, "Attribution error: " + s);
                }
            };

            AppsFlyerLib.getInstance().init(appsFlyerID, conversionListener, this);
            AppsFlyerLib.getInstance().setDebugLog(true);
            isAFInitialized = true;
        }
        else if(!isNetworkConnected())
        {
            showNoInternetDialog();
        }
    }

    public synchronized void startAF(String appsFlyerID)
    {
        if(isAFInitialized && !isAFStarted)
        {
            AppsFlyerLib.getInstance().start(this, appsFlyerID, new AppsFlyerRequestListener() {
                @Override
                public void onSuccess() {
                    Log.d(LOG_TAG, "AF Initialized Successfully");
                    isAFStarted = true;
                }

                @Override
                public void onError(int i, @NonNull String s) {
                    Log.d(LOG_TAG, "AF Not Initialized.\n\nError Code: " + i + "\nError Message: " + s);
                    isAFStarted = false;
                }
            });
        }
        else
        {
            Log.d(LOG_TAG,"AF cannot be started. It has not been initialized yet, or has been started already");
        }
    }

    public boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            Network network = connectivityManager.getActiveNetwork();
            if (network != null) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
                return capabilities != null &&
                        (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN));
            }
        }
        return false;
    }

    public void showNoInternetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.network_ui, null);
        builder.setView(dialogView);
        Button retryButton = dialogView.findViewById(R.id.retryButton);
        Button closeButton = dialogView.findViewById(R.id.closeButton);
        retryButton.setOnClickListener(v -> AppConfig.getInstance().isNetworkConnected());
        closeButton.setOnClickListener(v -> System.exit(1));
        builder.setCancelable(false);
        builder.show();
    }
}
