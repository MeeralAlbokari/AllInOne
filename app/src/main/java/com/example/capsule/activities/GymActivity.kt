package com.example.capsule.activities
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.capsule.R
import com.example.capsule.databinding.ActivityGymBinding
import com.example.capsule.retrofit.exerciseAPI.APIClient
import com.example.capsule.retrofit.exerciseAPI.APIInterface
import com.example.capsule.retrofit.exerciseModel.GooglePlaces
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GymActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityGymBinding

    lateinit var currentLocation: GooglePlaces
    private lateinit var lastLocation: Location
    private var mMarker: Marker?=null
    var lati=0.0
    var longi=0.0

    //location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallBack: LocationCallback


    companion object{
        private const val MY_PREMISSION_CODE=1000
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGymBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            checkPremission()
        }
        buildLocationRequest()
        buildLocationCallBack()

        fusedLocationClient= LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest,locationCallBack, Looper.myLooper())


    }// on create


    //--------------for the current location---------
    private fun buildLocationCallBack() {

        locationCallBack = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                lastLocation=p0!!.locations.get(p0!!.locations.size-1)
                if (mMarker!=null){
                    mMarker!!.remove()
                }
                lati= lastLocation.latitude
                longi=lastLocation.longitude

                val  latLng=LatLng(lati,longi)
                Log.d("coord", "onLocationResult: $latLng")
                val merkerOptions= MarkerOptions()
                    .position(latLng)
                    .title("you're location")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                mMarker=mMap!!.addMarker(merkerOptions)
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                mMap!!.animateCamera(CameraUpdateFactory.zoomTo(15f))
                nearByGym()
            }
        }

    }

    private fun buildLocationRequest() {

        locationRequest=LocationRequest()
        locationRequest.priority= LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval=5000
        locationRequest.fastestInterval=3000
        locationRequest.smallestDisplacement=10f

    }

    private fun checkPremission():Boolean {

        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION))
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PREMISSION_CODE
                )

            else
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PREMISSION_CODE
                )
            return false
        }
        else
            return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            MY_PREMISSION_CODE -> {
                if (grantResults.size>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
                {
                    if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
                    {
                        if (checkPremission()){
                            mMap!!.isMyLocationEnabled=true
                        }
                    }
                }
                else{
                    Toast.makeText(this,"permission required", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                mMap!!.isMyLocationEnabled=true
            }

        }
        else
            mMap!!.isMyLocationEnabled=true

        mMap.uiSettings.isZoomControlsEnabled=true

    }// maps


    //-----------main fun----------
    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallBack)
        Log.d("TAG", "stopLocationUpdates: Location Update Stop")
    }
    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }
    override fun onResume() {
        super.onResume()
        if (fusedLocationClient!= null) {
//            startLocationUpdates()
            mMarker?.remove()
        }// if
    }// resume



    private fun nearByGym(){

        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        var cLocation="${lati},${longi}"
        Log.d("Location", "nearByGym: $cLocation")
        val type="gym"
        val API_KEY="AIzaSyClXdlHlo8KwNn3kj1jOPTGgkzpm9PdYdk"
        var raduis=1500

        apiInterface?.getNearBy(cLocation,raduis,type,API_KEY)?.enqueue(object :Callback<GooglePlaces>{
            override fun onResponse(call: Call<GooglePlaces>, response: Response<GooglePlaces>) {
                currentLocation= response.body()!!
                Log.d("check", "onResponse: $currentLocation")
                if (response!!.isSuccessful){
                    for (i in 0..currentLocation.results.lastIndex){

                        val merakerOptions=MarkerOptions()
                        val googlePlace= currentLocation.results[i]
                        val lat= googlePlace.geometry.location.lat
                        val lng= googlePlace.geometry.location.lng
                        val name=googlePlace.name
//                        val info="${googlePlace.opening_hours}\n ${googlePlace.price_level}\n ${googlePlace.rating}"
                        val latLng=LatLng(lat,lng)
                        merakerOptions.position(latLng)
                        merakerOptions.title(name)
//                        merakerOptions.snippet(info)
                        merakerOptions.icon(
                            BitmapDescriptorFactory.defaultMarker(
                                BitmapDescriptorFactory.HUE_MAGENTA))
                        mMap.addMarker(merakerOptions)
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                        mMap!!.animateCamera(CameraUpdateFactory.zoomTo(15f))
                    }//for
                }
            }

            override fun onFailure(call: Call<GooglePlaces>, t: Throwable) {
                Toast.makeText(this@GymActivity, "$t", Toast.LENGTH_LONG).show()
                Log.d("error", "onFailure: $t")
            }

        })

    }


}