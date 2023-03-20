package com.kiki.searchcustomization.ui.splash

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.kiki.searchcustomization.databinding.ActivitySplashBinding
import com.kiki.searchcustomization.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySplashBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<SplashViewModel>()
    private val fusedLocation by lazy { LocationServices.getFusedLocationProviderClient(this) }

    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            requestLocation()
        } else {
            getMyLastLocation()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        getMyLastLocation()
    }

    private fun getMyLastLocation() {
        val locationRequest = LocationRequest.Builder(100)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()

        val locationSettings = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .build()
        LocationServices.getSettingsClient(this)
            .checkLocationSettings(locationSettings)
            .addOnSuccessListener { requestLocation() }
            .addOnFailureListener {
                if ((it as ResolvableApiException).statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                    MaterialAlertDialogBuilder(this)
                        .setTitle("Aktifkan Lokasi")
                        .setMessage("Aplikasi memerlukan lokasi saat ini untuk menghitung jarak warteg.")
                        .setPositiveButton("Aktifkan") { _, _ ->
                            try {
                                launcher.launch(
                                    IntentSenderRequest.Builder(it.resolution).build()
                                )
//                                it.startResolutionForResult(this, 61124)
                            } catch (e: IntentSender.SendIntentException) {
                                it.localizedMessage?.let { message ->
                                    Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
                                        .show()
                                }
                            }
                        }
                        .show()
                } else {
                    it.localizedMessage?.let { message ->
                        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
    }

    private fun requestLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
        val tokenSource = CancellationTokenSource().token
        fusedLocation.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, tokenSource)
            .addOnSuccessListener { location ->
                val latLng = LatLng(location.latitude, location.longitude)
                viewModel.saveLatLng(latLng)
                lifecycleScope.launch {
                    viewModel.updateWartegDistance()
                    delay(3500)
                    startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                }
            }
    }

    private val permissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permission ->
        when {
            permission[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                getMyLastLocation()
            }
            permission[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                getMyLastLocation()
            }
            else -> {
                Toast.makeText(this, "Akses lokasi tidak diizinkan", Toast.LENGTH_SHORT).show()
            }
        }
    }
}