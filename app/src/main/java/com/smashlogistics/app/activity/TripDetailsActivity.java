package com.smashlogistics.app.activity;

import static com.smashlogistics.app.utility.SessionManager.currency;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.smashlogistics.app.R;
import com.smashlogistics.app.model.HistoryinfoItem;
import com.smashlogistics.app.retrofit.APIClient;
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

public class TripDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {


    SessionManager sessionManager;
    HistoryinfoItem item;

    @BindView(R.id.ima_back)
    ImageView imaBack;
    @BindView(R.id.txt_date)
    TextView txtDate;
    @BindView(R.id.txt_packid)
    TextView txtPackid;
    @BindView(R.id.txtttotle)
    TextView txtttotle;
    @BindView(R.id.img_icon)
    ImageView imgIcon;
    @BindView(R.id.txt_ridername)
    TextView txtRidername;
    @BindView(R.id.txt_vtype)
    TextView txtVtype;
    @BindView(R.id.txt_status)
    TextView txtStatus;
    @BindView(R.id.txt_pickaddress)
    TextView txtPickaddress;
    @BindView(R.id.txt_dropaddress)
    TextView txtDropaddress;
    @BindView(R.id.txt_porterfare)
    TextView txtPorterfare;
    @BindView(R.id.txt_coupondis)
    TextView txtCoupondis;
    @BindView(R.id.lvl_discount)
    LinearLayout lvlDiscount;
    @BindView(R.id.txt_wallet)
    TextView txtWallet;
    @BindView(R.id.lvl_wallet)
    LinearLayout lvlWallet;
    @BindView(R.id.lvl_rider)
    LinearLayout lvlRider;
    @BindView(R.id.txt_total)
    TextView txtTotal;
    @BindView(R.id.img_category)
    ImageView imgCategory;
    @BindView(R.id.txt_ctitle)
    TextView txtCTitle;
    @BindView(R.id.img_payment)
    ImageView imgPayment;
    @BindView(R.id.txt_paymenttitle)
    TextView txtPaymenttitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(this);
        item = getIntent().getParcelableExtra("myclass");

        Glide.with(this).load(APIClient.baseUrl + "/" + item.getRiderImg()).thumbnail(Glide.with(this).load(R.drawable.emty)).into(imgIcon);
        Glide.with(this).load(APIClient.baseUrl + "/" + item.getCatImg()).thumbnail(Glide.with(this).load(R.drawable.emty)).into(imgCategory);
        Glide.with(this).load(APIClient.baseUrl + "/" + item.getPMethodImg()).thumbnail(Glide.with(this).load(R.drawable.emty)).into(imgPayment);


        if (item.getOrderStatus().equalsIgnoreCase("cancelled")) {
            lvlRider.setVisibility(View.GONE);
        }
        txtttotle.setText(sessionManager.getStringData(currency) + item.getPTotal());
        txtDate.setText("" + item.getDateTime());
        txtPackid.setText("" + item.getPackageNumber());
        txtRidername.setText("" + item.getRiderName());
        txtVtype.setText("" + item.getvType());
        txtStatus.setText("" + item.getOrderStatus());
        txtPickaddress.setText("" + item.getPickAddress());
        txtDropaddress.setText("" + item.getDropAddress());
        txtPorterfare.setText("" + item.getSubtotal());
        txtTotal.setText(sessionManager.getStringData(currency) + item.getPTotal());
        txtPorterfare.setText(sessionManager.getStringData(currency) + item.getSubtotal());
        txtCTitle.setText("" + item.getCatName());
        txtPaymenttitle.setText("" + item.getPMethodName());

        if (Double.parseDouble(item.getCouAmt()) != 0) {
            lvlDiscount.setVisibility(View.VISIBLE);
            txtCoupondis.setText(sessionManager.getStringData(currency) + item.getCouAmt());
        }

        if (Double.parseDouble(item.getWallAmt()) != 0) {
            lvlWallet.setVisibility(View.VISIBLE);
            txtWallet.setText(sessionManager.getStringData(currency) + item.getWallAmt());
        }


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @OnClick({R.id.ima_back})
    public void onBindClick(View view) {
        if (view.getId() == R.id.ima_back) {
            finish();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        googleMap
                .addPolyline((new PolylineOptions())
                        .add(new LatLng(item.getPickLat(), item.getPickLng()), new LatLng(item.getDropLat(), item.getDropLng())).width(5).color(Color.BLUE)
                        .geodesic(true));
        // move camera to zoom on map

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(item.getPickLat(), item.getPickLng()), 10));

        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(item.getPickLat(), item.getPickLng()))
                .title("Pickup")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_current_long)));

        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(item.getDropLat(), item.getDropLng()))
                .title("Drop")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_destination_long)));
    }
}