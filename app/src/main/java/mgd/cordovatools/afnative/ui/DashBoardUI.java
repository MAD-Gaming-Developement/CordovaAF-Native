package mgd.cordovatools.afnative.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import mgd.cordovatools.afnative.R;
import mgd.cordovatools.afnative.frags.UIPagerAdapter;

public class DashBoardUI extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private LinearLayout navigationDrawer;
    private CoordinatorLayout coordinatorLayout;
    private BottomNavigationView bottomNavigationView;

    private ActionBarDrawerToggle toggle;

    private GestureDetector gestureDetector;
    private boolean isBottomNavVisible = false;

    public static void openDashboard(Context mContext)
    {
        Intent dashBoard = new Intent(mContext, DashBoardUI.class);
        dashBoard.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mContext.startActivity(dashBoard);
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        getWindow().setFlags(1024,1024);

        setContentView(R.layout.activity_dash_board_ui);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        drawerLayout = findViewById(R.id.main);
        coordinatorLayout = findViewById(R.id.coordinator_layout);
        navigationDrawer = findViewById(R.id.navigation_drawer);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Handle clicks on menu items
        TextView menuHome = findViewById(R.id.txtmenu_home);
        menuHome.setOnClickListener(v -> {
            // Replace main content with HomeFragment or handle navigation
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        TextView menuPrivacy = findViewById(R.id.txtmenu_privacy);
        menuPrivacy.setOnClickListener(v -> {
            // Replace main content with PrivacyFragment or handle navigation
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        TextView menuAbout = findViewById(R.id.txtmenu_about);
        menuAbout.setOnClickListener(v -> {
            // Replace main content with AboutFragment or handle navigation
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        TextView menuQuit = findViewById(R.id.txtmenu_quit);
        menuQuit.setOnClickListener(v -> {
            // Handle Quit functionality (e.g., exit the app)
            finish();
        });

        ViewPager viewPager = findViewById(R.id.view_pager);
        UIPagerAdapter uiAdapter = new UIPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(uiAdapter);

        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            private final int SWIPE_THRESHOLD = ViewConfiguration.get(DashBoardUI.this).getScaledTouchSlop();
            private final int SWIPE_VELOCITY_THRESHOLD = ViewConfiguration.get(DashBoardUI.this).getScaledMinimumFlingVelocity();

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (Math.abs(e1.getX() - e2.getX()) > SWIPE_THRESHOLD) {
                    return false;
                }
                float deltaY = e1.getY() - e2.getY();
                if (Math.abs(deltaY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (deltaY > 0) {
                        // Swipe up
                        hideBottomNavigation();
                    } else {
                        // Swipe down
                        showBottomNavigation();
                    }
                    return true;
                }
                return false;
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            if(item.getItemId() == R.id.menu_home)
                return true;
            else if(item.getItemId() == R.id.menu_privacy)
                return true;
            else if(item.getItemId() == R.id.menu_about)
                return true;
            else if(item.getItemId() == R.id.menu_quit) {
                finish();
            }
            else
                return false;

            hideBottomNavigation();
            return false;
        });
    }

    private void hideBottomNavigation() {
        if (isBottomNavVisible) {
            bottomNavigationView.animate()
                    .translationY(bottomNavigationView.getHeight())
                    .setDuration(300)
                    .withEndAction(() -> bottomNavigationView.setVisibility(View.GONE))
                    .start();
            isBottomNavVisible = false;
        }
    }

    private void showBottomNavigation() {
        if (!isBottomNavVisible) {
            bottomNavigationView.setVisibility(View.VISIBLE);
            bottomNavigationView.animate()
                    .translationY(0)
                    .setDuration(300)
                    .start();
            isBottomNavVisible = true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}