package pt.ipt.dama2024.gps

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), LocationListener {

    // auxiliary vars
    private lateinit var locationManager: LocationManager
    private lateinit var txtGpsLocation: TextView
    private val locationPermissionCode = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // finds the button and look for GPS coordinates
        val button: Button=findViewById(R.id.getLocation)
        button.setOnClickListener {
            getGPSLocation()
        }

    }

    /**
     * Get the GPS location
     *
     */
    private fun getGPSLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            requestPermissions(
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionCode
            )
        }
        /*  Inicia o GPS, que vai autilizar a posição de 5 em 5 segundos,
            se a nova localização estiver pelo menos a 5 metros da última
            localização  */
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
            5000, 5f, this)
    }

    /**
     * On location changed, add the GPS data to TextView
     *
     * @param location
     */
    override fun onLocationChanged(location: Location) {
        txtGpsLocation = findViewById(R.id.GPS_values)
        txtGpsLocation.text =
            "Latitude: ${location.latitude} \nLongitude: ${location.longitude}"
    }


    /**
     * Asks for authorization to access GPS
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }





}