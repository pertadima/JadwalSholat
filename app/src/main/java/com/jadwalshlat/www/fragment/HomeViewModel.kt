package com.jadwalshlat.www.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel;
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import com.jadwalshlat.www.jetpackcontroller.User
import com.jadwalshlat.www.jetpackcontroller.UserRepository
import org.json.JSONObject

class HomeViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    // 1
    private var userRepository: UserRepository? = null
    // 2
    private var scheduleDatabase: LiveData<User>? = null
    // 3
    private var scheduleListner = MutableLiveData<JSONObject>()

    fun init(userRepository: UserRepository) {
        this.userRepository = userRepository

        if (scheduleDatabase == null) {
            scheduleDatabase = userRepository.getUser("schedule")

        }

    }
    fun getScheduleDatabase(): LiveData<User>? { return scheduleDatabase }

    // ---------------------------------------------------------------------------------------------
    private fun requestPraySchedule(oData: JSONObject) {
        userRepository!!.requestPraySchedule(oData, scheduleListner)
    }

    fun getScheduleListener(): LiveData<JSONObject> { return scheduleListner }

    // ---------------------------------------------------------------------------------------------
    @SuppressLint("MissingPermission")
    fun getLocation(locationManager: LocationManager) {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000,
                10f, object : LocationListener {
            override fun onLocationChanged(location: Location?) {
                val latitude = location?.latitude
                val longitude = location?.longitude

                val oData = JSONObject()
                oData.put("lat", latitude)
                oData.put("lng", longitude)
                requestPraySchedule(oData)

            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

            }

            override fun onProviderEnabled(provider: String?) {

            }

            override fun onProviderDisabled(provider: String?) {

            }
        })

    }

    fun enableGPS(context: Context) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS") { _, _ ->
                    val callGPSSettingIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    context.startActivity(callGPSSettingIntent)
                }
                .setNegativeButton("Cancel") { dialog, _ -> dialog!!.cancel() }
        val alert = alertDialogBuilder.create()
        alert.show()

    }

}
