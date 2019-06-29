package com.jadwalshlat.www.jetpackcontroller

class JadwalSholat (
    var data: Data,
    var time: Time
)

class Data (
    var Fajr: String,
    var Dhuhr: String,
    var Asr: String,
    var Maghrib: String,
    var Isha: String
)

class Time (
        var date: String
)