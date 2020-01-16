package com.apsara.googlemap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private AutoCompleteTextView etcity;
    private Button btnSerach;
    private List<LatitudeLongitude> latitudeLongitudeList;
    Marker markerName;
    CameraUpdate center, zoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        etcity = findViewById(R.id.etCity);
        btnSerach = findViewById(R.id.btnSearch);
        fillArrayListAndSetAdapter();
        btnSerach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etcity.getText().toString())) {
                    etcity.setError("Please enter a place name");
                    return;
                }
                int position = SearchArrayList(etcity.getText().toString());
                if (position > -1)
                    loadMap(position);
                else
                    Toast.makeText(SearchActivity.this, "Location not found by name:"
                            + etcity.getText().toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void fillArrayListAndSetAdapter() {
        latitudeLongitudeList = new ArrayList<>();
        latitudeLongitudeList.add(new LatitudeLongitude(27.7134481, 85.3241922, "Naagpokhari"));
        latitudeLongitudeList.add(new LatitudeLongitude(27.7181749, 85.3173212, "Narayanhiti Palace Museum"));
        latitudeLongitudeList.add(new LatitudeLongitude(27.7127827, 85.3265391, "Hotel Brihaspati"));
        String[] data = new String[latitudeLongitudeList.size()];
        for (int i = 0; i < data.length; i++) {
            data[i] = latitudeLongitudeList.get(i).getMarker();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                SearchActivity.this,
                android.R.layout.simple_expandable_list_item_1,
                data
        );

        etcity.setAdapter(adapter);
        etcity.setThreshold(1);
    }

    public int SearchArrayList(String name) {
        for (int i = 0; i < latitudeLongitudeList.size(); i++) {
            if (latitudeLongitudeList.get(i).getMarker().contains(name)) {
                return i;

            }
        }
        return -1;
    }
    @Override
    public void onMapReady (GoogleMap googleMap){
        mMap=googleMap;
        center= CameraUpdateFactory.newLatLng(new LatLng(27.7172453, 85.3239605));
        zoom=CameraUpdateFactory.zoomTo(15);
        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }
    public void loadMap(int position){
        if (markerName!=null)
        {
            markerName.remove();
        }
        double latitude=latitudeLongitudeList.get(position).getLat();
        double longitude=latitudeLongitudeList.get(position).getLon();
        String marker=latitudeLongitudeList.get(position).getMarker();
        center=CameraUpdateFactory.newLatLng(new LatLng(latitude,longitude));
        zoom=CameraUpdateFactory.zoomTo(17);
        markerName=mMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title(marker));
        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

}
