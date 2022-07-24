package com.example.nearbyplaces

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location

import com.google.android.gms.location.LocationRequest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.nearbyplaces.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.net.PlacesClient
import com.mancj.materialsearchbar.MaterialSearchBar


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var latitude: Double = 0.toDouble()
    private var longitude: Double = 0.toDouble()
    private lateinit var mLastLocation: Location
    private var mMarker: Marker? = null

    //Location
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback


    //TODO suggestions and search bar is pending for now
    //loading suggestions when user searches any place.
    private lateinit var PlacesClient: PlacesClient
    private lateinit var mylist: List<AutocompletePrediction>

    private lateinit var MaterialSearchBar: MaterialSearchBar

    private var DEFAULT_ZOOM = 18


    companion object {
        private const val MY_PERMISSION_CODE: Int = 1000
    }


    private lateinit var binding: ActivityMapsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //Request runtime permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkLocationPermission()) {
                buildLocationRequest();
                buildLocationCallBack();
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.myLooper()
                );

            }

        } else {
            buildLocationRequest();
            buildLocationCallBack();
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.myLooper()
            );
        }


        //assigning functions to the bottom navigation buttons.

        binding.bottomNavigation.setOnNavigationItemReselectedListener{
            when(it.itemId){
                R.id.action_market-> MynearbyPlaces("market")
                R.id.action_airport-> MynearbyPlaces("airport")
                R.id.action_restaurant-> MynearbyPlaces("restaurant")
                R.id.action_hotel-> MynearbyPlaces("hotel")
                R.id.action_hospital-> MynearbyPlaces("hospital")

            }
        }


    }

     fun MynearbyPlaces(str: String){
         //TODO from here we will implement our nearby locations.
    }


    private fun buildLocationCallBack() {
        locationCallback=object:LocationCallback(){
            override fun onLocationResult(p0: LocationResult) {
              mLastLocation=p0!!.locations.get(p0!!.locations.size-1) //Get Last Location
                if(mMarker !=null){
                    mMarker!!.remove()
                }
                latitude=mLastLocation.latitude
                longitude=mLastLocation.longitude

                val latLng=LatLng(latitude, longitude)
                val markerOptions=MarkerOptions()
                    .position(latLng)
                    .title("Your Position")
                    //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))

                mMarker=mMap!!.addMarker(markerOptions)

                //Move camera
                mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                mMap!!.animateCamera(CameraUpdateFactory.zoomTo(11f))
            }


        }

    }

    private fun buildLocationRequest() {
       locationRequest= LocationRequest()
        locationRequest.priority=LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval=5000
        locationRequest.fastestInterval=3000
        locationRequest.smallestDisplacement=10f

    }

    private fun checkLocationPermission():Boolean {
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.ACCESS_FINE_LOCATION))
                ActivityCompat.requestPermissions(this, arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ),MY_PERMISSION_CODE)
            else
                ActivityCompat.requestPermissions(this, arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ),MY_PERMISSION_CODE)

                return false


        }
         return true



    }

    //override onRequestPermissionResult

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
      when(requestCode){
          MY_PERMISSION_CODE->{
              if(grantResults.size>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                  if (ContextCompat.checkSelfPermission(
                          this,
                          android.Manifest.permission.ACCESS_FINE_LOCATION
                      ) == PackageManager.PERMISSION_GRANTED
                  )
                      if (checkLocationPermission()){
                          buildLocationRequest();
                          buildLocationCallBack();
                          fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
                          fusedLocationProviderClient.requestLocationUpdates(
                              locationRequest,
                              locationCallback,
                              Looper.myLooper()
                          );
                          mMap!!.isMyLocationEnabled = true
              }
                  else{
                      Toast.makeText(this,"Permission Denied", Toast.LENGTH_SHORT).show()
                      }
              }

          }
      }
    }

    override fun onStop() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        super.onStop()
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

       //Init google play services

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                mMap!!.isMyLocationEnabled = true

            }

        }
        else mMap!!.isMyLocationEnabled=true

        //Unable zoom control
        mMap.uiSettings.isZoomControlsEnabled=true
    }
}















