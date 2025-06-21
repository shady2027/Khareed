package com.example.khareed.Location

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.khareed.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import android.location.Geocoder
import android.location.Location
import android.widget.Button
import android.widget.TextView
import com.example.khareed.Activity.MainActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.io.IOException
import java.util.Locale

class LocationActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var smf: SupportMapFragment
    private lateinit var client: FusedLocationProviderClient
    private lateinit var currentLocationText : TextView
    private lateinit var save : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        currentLocationText = findViewById(R.id.currentLocationText)
        save = findViewById(R.id.saveButton)
        save.setOnClickListener{
            navigateToMainActivity()
        }
        sharedPreferences = getSharedPreferences("LocationPreferences", Context.MODE_PRIVATE)
        smf = supportFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment
        client = LocationServices.getFusedLocationProviderClient(this)

        Dexter.withContext(applicationContext)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(permissionGrantedResponse: PermissionGrantedResponse) {
                    getMyLocation()
                }

                override fun onPermissionDenied(permissionDeniedResponse: PermissionDeniedResponse) {}

                override fun onPermissionRationaleShouldBeShown(
                    permissionRequest: PermissionRequest,
                    permissionToken: PermissionToken
                ) {
                    permissionToken.continuePermissionRequest()
                }
            }).check()

    }

    private fun getMyLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request location permissions here if not granted already
            return
        }

        val task: Task<Location> = client.lastLocation
        task.addOnSuccessListener(OnSuccessListener<Location> { location ->
            smf.getMapAsync(OnMapReadyCallback { googleMap ->
                val latLng = LatLng(location.latitude, location.longitude)
                val markerOptions = MarkerOptions().position(latLng).title("You are here...!!")
                googleMap.addMarker(markerOptions)
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                // Get address from latitude and longitude
                val geocoder = Geocoder(this)
                try {
                    val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    if (addresses != null) {
                        if (addresses.isNotEmpty()) {
                            val address = addresses[0]
                            val addressText = "${address.getAddressLine(0)}, ${address.locality}, ${address.adminArea}, ${address.countryName}"
                            // Display address in a TextView
                            currentLocationText.text = "Current Location :" + addressText
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            })
        })
    }

    private fun saveLocation(address: String) {
        val editor = sharedPreferences.edit()
        editor.putString("userLocation", address)
        editor.apply()
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}