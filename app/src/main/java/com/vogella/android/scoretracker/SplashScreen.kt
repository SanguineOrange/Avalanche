package com.vogella.android.scoretracker

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.getSystemService
import com.vogella.android.scoretracker.data.StatsNhlApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.pow

class SplashScreen : AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer : Sensor
    private lateinit var mImage : ImageView
    var x : Float = 0.0F
    var y : Float = 0.0F
 //   val logoMoving : Drawable = resources.getDrawable(R.drawable.ic_colorado_avalanche_oldalt)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //sensors to bounce the logo if loading fails
        sensorManager =  getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        //hides title for splash screen and makes it fullscreen
        window.requestFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash_screen)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://statsapi.web.nhl.com/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //only launch the app proper if there is internet connection
        val cM : ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(cM.activeNetwork !=  null){
            Handler().postDelayed({
                startActivity(Intent(this@SplashScreen, MainActivity::class.java))
                finish()
            }, 5000)
        }

        else{
            val loadText = findViewById<TextView>(R.id.textView4)
            loadText.text = getString(R.string.loading_error)
            AlertDialog.Builder(this)
                .setMessage("Could not connect to NHL.com! Please close the app and try again!")

        }
    }

    //moves the logo along with accelerometer values
    override fun onSensorChanged(p0: SensorEvent?) {
        if(p0!!.sensor.type == Sensor.TYPE_ACCELEROMETER){
            val logo = findViewById<ImageView>(R.id.splashLogo)
             x -= p0.values[0]*1.2F
             y += p0.values[1]*1.2F

            logo.y = y
            logo.x =x
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {   }

    //this code adapted from https://stackoverflow.com/questions/6457768/moving-an-image-using-accelerometer-of-android/14804196
    override fun onResume() {
        super.onResume()
        // Register this class as a listener for the accelerometer sensor
        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL
        )
        // ...and the orientation sensor
        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }
}
