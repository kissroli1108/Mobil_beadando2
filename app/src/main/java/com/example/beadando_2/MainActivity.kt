package com.example.beadando_2

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.*

import org.json.JSONException

import org.json.JSONObject
import java.time.Duration
import java.time.Duration.parse
import java.time.LocalTime
import java.time.OffsetDateTime
import kotlin.math.sign


class MainActivity : AppCompatActivity() {
    lateinit var textView: TextView
    lateinit var textView3: TextView
    lateinit var button: Button
    lateinit var editText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)

        textView3 = findViewById(R.id.textView3)
        button = findViewById(R.id.button)

        editText = findViewById(R.id.editText)


    }

    fun handleButton(view: android.view.View) {
        jsonParse()
    }

    private fun jsonParse() {

        val queue = Volley.newRequestQueue(this)
        var city = editText.text.toString().replace(" ","%20")

        var urlgeo="https://api.mapbox.com/geocoding/v5/mapbox.places/$city.json?access_token=pk.eyJ1Ijoia2lzc3JvbGFuZDk5IiwiYSI6ImNrd3Y4OHc3cjE0c28yeW1oNnFocnl6emcifQ.UBfg_cm1M5i3OPkeTP-5Yg"
        var urlSun = "https://api.sunrise-sunset.org/json?"
        var urlTime = "https://www.timeapi.io/api/Time/current/coordinate?"



        var request = JsonObjectRequest(
            Request.Method.GET, urlgeo,null,
            { response ->

                try {

                    val jsonObj = response.getJSONArray("features")
                    val jsonarr = jsonObj.getJSONObject(0)
                    val loc = jsonarr.getJSONArray("center")
                    val lat = loc.getString(1)
                    val long = loc.getString(0)

                    urlTime+=("latitude=$lat&longitude=$long")
                    urlSun+=("lat=$lat&lng=$long")
                    sunTime(urlSun)
                    currTime(urlTime)

                } catch (e: JSONException) {
                    print("json failed 1")
                }

            }) { error -> print("json failed 2") }

        queue.add(request)

    }


    private fun sunTime(url : String){
        val queue = Volley.newRequestQueue(this)

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val jsonObj = response.getJSONObject("results")

                    val sunrise = jsonObj.getString("sunrise")
                    val sunset = jsonObj.getString("sunset")
                    val dayLength = jsonObj.getString("day_length")
                    textView3.text=""
                    textView3.append("Sunrise: $sunrise\nSunset: $sunset\nLength of the day: $dayLength")

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }) { error -> error.printStackTrace() }
        queue.add(request)
    }

    private fun currTime(url : String){
        val queue = Volley.newRequestQueue(this)

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val time = response.getString("time")
                    textView.text=""
                    textView.append("\nCurrent time: $time")

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }) { error -> error.printStackTrace() }

        queue.add(request)

    }
}

