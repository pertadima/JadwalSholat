package com.jadwalshlat.www.jetpackcontroller

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import org.jetbrains.anko.doAsync
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import javax.inject.Singleton

@Singleton
class UserRepository(private val webservice: Webservice, private val userDao: UserDao) {
    fun setUser(user: User) {
        doAsync {
            userDao.createUser(user)

        }

    }

    fun getUser(name: String) : LiveData<User> {
        return userDao.readUser(name)

    }

    fun requestPraySchedule(locationData: JSONObject, listener: MutableLiveData<JSONObject>) {
        val lat = locationData.getString("lat")
        val lng = locationData.getString("lng")

        val aData = JSONObject()
        aData.put("isSuccess", false)
        webservice.getRoom(lat, lng).enqueue(object : retrofit2.Callback<JadwalSholat> {
            override fun onFailure(call: Call<JadwalSholat>, t: Throwable) {
                aData.put("responseData", t.message)
                listener.value = aData

            }

            override fun onResponse(call: Call<JadwalSholat>, response: Response<JadwalSholat>) {
                if (response.code() == 200) {
                    val mResponse = response.body()!!.data
                    val mResponse2 = response.body()!!.time

                    if ((mResponse != null) && (mResponse2 != null)) {
                        aData.put("isSuccess", true)

                        val mData = JSONObject()
                        mData.put("subuh", mResponse.Fajr)
                        mData.put("zuhur", mResponse.Dhuhr)
                        mData.put("asar", mResponse.Asr)
                        mData.put("magrib", mResponse.Maghrib)
                        mData.put("isya", mResponse.Isha)
                        Log.d("JADWALSHOLAT", "UserRepository/55 : ${mResponse.Isha}")

                        val iTime = mResponse2.date
                        val dateFormatter = SimpleDateFormat("yyyy-MM-dd")
                        val date = dateFormatter.parse(iTime)
                        val timeFormatter = SimpleDateFormat("d MMM yyyy")
                        val displayValue = timeFormatter.format(date)
                        mData.put("date", "Lokasi anda\n$displayValue")

                        aData.put("responseData", mData.toString())
                        setUser(User("schedule", aData.toString()))

                    } else {
                        aData.put("responseData", "Data error")

                    }

                } else {
                    aData.put("responseData", response.code().toString())

                }

                listener.value = aData

            }


        })

    }

}