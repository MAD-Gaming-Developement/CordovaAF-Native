package mgd.cordovatools.afnative.ui;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import mgd.cordovatools.afnative.AppConfig;
import mgd.cordovatools.afnative.R;
import mgd.cordovatools.afnative.helpers.SyncManager;

public class SplashUI extends AppCompatActivity {

    private SyncManager syncManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Set UI Settings
        getWindow().setFlags(1024, 1024);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        setContentView(R.layout.activity_splash_ui);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if(AppConfig.getInstance().isNetworkConnected())
        {
            VideoView videoView = findViewById(R.id.splashUI);
            Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.intro);
            videoView.setVideoURI(videoUri);
            videoView.setOnCompletionListener(mediaPlayer -> {
                syncManager = new SyncManager(SplashUI.this);
                syncManager.checkForUpdates();
            });
            videoView.setOnPreparedListener(mediaPlayer -> mediaPlayer.setLooping(false));
            videoView.start();
        }
        else
        {
            AppConfig.getInstance().showNoInternetDialog();
        }
    }

}