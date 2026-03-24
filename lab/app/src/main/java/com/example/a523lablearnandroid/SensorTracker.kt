package com.example.a523lablearnandroid

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class SensorTracker(context: Context) {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private var listener: SensorEventListener? = null

    fun startListening(onSensorChanged: (FloatArray) -> Unit) {
        if (accelerometer == null) return

        listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
                    onSensorChanged(event.values)
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                // ไม่จำเป็นต้องใช้สำหรับโจทย์นี้
            }
        }
        sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_UI)
    }

    fun stopListening() {
        listener?.let {
            sensorManager.unregisterListener(it)
        }
    }
}
