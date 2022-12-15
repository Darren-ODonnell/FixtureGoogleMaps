package com.example.googlemapsproject;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.googlemapsproject.Models.Club;
import com.example.googlemapsproject.Models.Fixture;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.googlemapsproject.databinding.ActivityMapsBinding;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    Fixture currentFixture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent i = getIntent();
        currentFixture = (Fixture) i.getSerializableExtra("currentFixture");

        binding.button2.setOnClickListener(v -> startActivity(new Intent(this,FixtureActivity.class)));

        //TODO Get Long Lat from Intent

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        String location = currentFixture.getHomeTeam().getPitches();

        // Add a marker in Sydney and move the camera
        LatLng dublin = getLocationFromString(location);

        mMap.addMarker(new MarkerOptions().position(dublin).title("Marker in " + location));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dublin));
        mMap.setMinZoomPreference(10);
    }

    public LatLng getLocationFromString(String attraction){
        // retrieve latitude and longitude of city/attraction
        Geocoder coder = new Geocoder( this );
        double latitude = 53;
        double longitude = -6;
        try { // geocode city
            List<Address> locations =
                    coder.getFromLocationName(attraction, 2); //return max 2
            //coordinates for this address, the first address is the best location
            if (locations != null) {
                latitude = locations.get(0).getLatitude();
                longitude = locations.get(0).getLongitude();
                Toast.makeText(this, String.valueOf(latitude) + "' "
                        +String.valueOf(longitude), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Not found location", Toast.LENGTH_LONG).show();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return new LatLng(latitude, longitude);

    }
}