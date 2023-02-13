package com.smashlogistics.app.activity;

import static com.smashlogistics.app.utility.SessionManager.login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.smashlogistics.app.R;
import com.smashlogistics.app.fregment.AccountFragment;
import com.smashlogistics.app.fregment.HomeFragment;
import com.smashlogistics.app.fregment.TripFragment;
import com.smashlogistics.app.fregment.WalletFragment;
import com.smashlogistics.app.model.SelectAddress;
import com.smashlogistics.app.utility.SessionManager;
import com.smashlogistics.app.utility.Utility;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    @BindView(R.id.container)
    FrameLayout container;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigation;
    SessionManager sessionManager;
    SelectAddress selectAddress;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);
        sessionManager = new SessionManager(HomeActivity.this);

        bottomNavigation.setOnNavigationItemSelectedListener(this);

        requestPermissions(new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        final LocationManager manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && Utility.hasGPSDevice(this)) {
            Toast.makeText(this, "Gps not enabled", Toast.LENGTH_SHORT).show();
            Utility.enableLoc(this);
        } else {
            openFragment(new HomeFragment(), "Home");
        }
    }
    public void openFragment(Fragment fragment, String tag) {
        try {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, fragment, tag);
            transaction.addToBackStack(null);
            transaction.commit();
        }catch (Exception e){
            Log.e("Error","-->"+e.getMessage());
        }

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:

                HomeFragment home = (HomeFragment) getSupportFragmentManager().findFragmentByTag("Home");
                if (home != null && home.isVisible()) {
                    //DO STUFF
                    return false;
                } else {
                    openFragment(home, "Home");
                }
                return true;
            case R.id.navigation_orders:
                if (sessionManager.getBooleanData(login)) {
                    openFragment(new TripFragment(), "Tripe");
                } else {
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                }
                return true;
            case R.id.navigation_wallet:
                if (sessionManager.getBooleanData(login)) {
                    openFragment(new WalletFragment(), "Wallet");
                } else {
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                }
                return true;
            case R.id.navigation_user:
                if (sessionManager.getBooleanData(login)) {
                    openFragment(new AccountFragment(), "Account");
                } else {
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                }
                return true;


            default:
                return false;
        }
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}