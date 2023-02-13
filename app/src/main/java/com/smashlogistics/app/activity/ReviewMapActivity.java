package com.smashlogistics.app.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.smashlogistics.app.R;
import com.smashlogistics.app.utility.Drop;
import com.smashlogistics.app.utility.Pickup;
import com.smashlogistics.app.utility.SessionManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReviewMapActivity extends AppCompatActivity implements OnMapReadyCallback {


    @BindView(R.id.txt_pickaddress)
    TextView txtPickaddress;
    @BindView(R.id.txt_dropaddress)
    TextView txtDropaddress;
    @BindView(R.id.btn_proce)
    TextView btnProce;
    @BindView(R.id.lvl_view)
    LinearLayout lvlView;
    Pickup pickup;
    Drop drop;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_map);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(this);

        pickup = getIntent().getParcelableExtra("pickup");
        drop = getIntent().getParcelableExtra("drop");
        txtPickaddress.setText("" + pickup.getAddress());
        txtDropaddress.setText("" + drop.getAddress());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @OnClick({R.id.btn_proce})
    public void onBindClick(View view) {
        if (view.getId() == R.id.btn_proce) {
            startActivity(new Intent(this, BookWheelerActivity.class)
                    .putExtra("pickup", pickup)
                    .putExtra("drop", drop));
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMap
                .addPolyline((new PolylineOptions())
                        .add(new LatLng(pickup.getLat(), pickup.getLog()), new LatLng(drop.getLat(), drop.getLog())).width(5).color(Color.BLUE)
                        .geodesic(true));
        // move camera to zoom on map

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(pickup.getLat(), pickup.getLog()), 13));

        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(pickup.getLat(), pickup.getLog()))
                .title("Pickup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_current_long)));


        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(drop.getLat(), drop.getLog()))
                .title("Drop")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_destination_long)));
    }
}