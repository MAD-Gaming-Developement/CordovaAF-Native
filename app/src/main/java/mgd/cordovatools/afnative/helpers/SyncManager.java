package mgd.cordovatools.afnative.helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import mgd.cordovatools.afnative.AppConfig;
import mgd.cordovatools.afnative.ui.DashBoardUI;
import mgd.cordovatools.afnative.ui.SplashUI;
import mgd.cordovatools.afnative.ui.WebUI;

public class SyncManager {
    private Context context;
    private DBHelper dbHelper;
    private RequestQueue requestQueue;

    public SyncManager(Context context) {
        this.context = context;
        this.dbHelper = new DBHelper(context);
        this.requestQueue = Volley.newRequestQueue(context);
    }

    public void checkForUpdates() {
        String url = "https://mgdplaygames.pro/getdbversion?cli=" + context.getPackageName();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {
                    try {
                        int remoteVersion = response.getInt("dbversion");
                        int localVersion = dbHelper.getDbVersion();

                        if (remoteVersion != localVersion) {
                            syncData();
                        } else {
                            openAppropriateActivity();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> error.printStackTrace());

        requestQueue.add(jsonObjectRequest);
    }

    private void syncData() {
        String url = "https://mgdplaygames.pro/getdata?cli=" + context.getPackageName();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {
                    try {

                        JSONObject apiData = new JSONObject(response.getString("record"));
                        Log.d(AppConfig.LOG_TAG, "API Response: " + apiData);

                        int version = apiData.getInt("dbversion");
                        String link = apiData.getString("gamelink");
                        String policy = apiData.getString("policylink");
                        int status = apiData.getInt("status");

                        dbHelper.updateData(version, link, policy, status);
                        openAppropriateActivity();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> error.printStackTrace());

        requestQueue.add(jsonObjectRequest);
    }

    private void openAppropriateActivity() {
        Cursor cursor = dbHelper.getData();
        if (cursor.moveToFirst()) {
            Log.d(AppConfig.LOG_TAG, "Checked activity to start.");
            @SuppressLint("Range") String link = cursor.getString(cursor.getColumnIndex("dblink"));
            @SuppressLint("Range") int status = cursor.getInt(cursor.getColumnIndex("dbstatus"));
            cursor.close();

            Log.d(AppConfig.LOG_TAG, "Status: " + status);

            if (status == 0) {
                WebUI.openURL(context, link);
            } else {
                DashBoardUI.openDashboard(context);
            }
        }
        else
        {
            WebUI.openURL(context, "");
            //DashBoardUI.openDashboard(context);
        }
    }
}
