package com.francescosalamone.motog14issue

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    private val sensorManager by lazy {
        getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    private lateinit var textView: TextView

    private val gameRotationListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            val rotationMatrix = FloatArray(9)
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)

            textView.text = "${rotationMatrix.first()} \n${textView.text}"
        }
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }

    private val rotationListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {}
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.sensor_values)

        findViewById<Button>(R.id.fix_button)
            .setOnClickListener {
                listenRotationSensor()
            }
    }

    override fun onResume() {
        super.onResume()
        listenGameRotationSensor()
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(gameRotationListener)
        sensorManager.unregisterListener(rotationListener)
    }

    private fun listenGameRotationSensor() {
        sensorManager.registerListener(
            gameRotationListener,
            sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR),
            SensorManager.SENSOR_DELAY_NORMAL,
            20_000
        )
    }

    private fun listenRotationSensor() {
        sensorManager.registerListener(
            rotationListener,
            sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
            SensorManager.SENSOR_DELAY_NORMAL,
            20_000
        )
    }
}