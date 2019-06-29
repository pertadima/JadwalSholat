package com.jadwalshlat.www.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jadwalshlat.www.MainActivity

import com.jadwalshlat.www.R
import kotlinx.android.synthetic.main.home_fragment.*
import org.json.JSONObject



class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel
    private lateinit var locationManager: LocationManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(HomeViewModel::class.java)
        // TODO: Use the ViewModel
        viewModel.init((activity as MainActivity).userRepository)
        viewModel.getScheduleDatabase()?.observe(this, Observer {
            if (it != null) {
                val jData = JSONObject(it.value)
                if (jData.getBoolean("isSuccess")) {
                    val xData = JSONObject(jData.getString("responseData"))
                    val place = xData.getString("date")
                    val subuh = xData.getString("subuh")
                    val zuhur = xData.getString("zuhur")
                    val asar = xData.getString("asar")
                    val magrib = xData.getString("magrib")
                    val isya = xData.getString("isya")

                    textView_home_fragment_1_2.text = place

                    textView_home_fragment_1_4.text = subuh
                    textView_home_fragment_1_6.text = zuhur
                    textView_home_fragment_1_8.text = asar
                    textView_home_fragment_1_10.text = magrib
                    textView_home_fragment_1_12.text = isya

                    (activity as MainActivity).stopLoading()

                } else {
                    Log.d("JADWALSHOLAT", "HomeFragment/49 : Error gan")

                }

            }

        })

        viewModel.getScheduleListener().observe(this, Observer {
            if (it != null) {
                if (!it.getBoolean("isSuccess")) {
                    (activity as MainActivity).popUp(it.getString("responseData"))

                }

            }

        })

        locationManager = context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
                getPermission(locationManager)

            } else{
                viewModel.getLocation(locationManager)
            }
        } else { viewModel.enableGPS(context!!) }

    }

    fun getPermission(locationManager: LocationManager) {
        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)

        } else {
            viewModel.getLocation(locationManager)
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            100 -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    viewModel.getLocation(locationManager)
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }

    }

}
