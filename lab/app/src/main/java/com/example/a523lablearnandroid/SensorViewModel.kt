package com.example.a523lablearnandroid

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SensorViewModel(application: Application) : AndroidViewModel(application) {
    private val sensorTracker = SensorTracker(application)

    // _sensorData เป็นท่อน้ำที่เก็บค่าล่าสุดไว้
    private val _sensorData = MutableStateFlow(FloatArray(3) { 0f })
    val sensorData: StateFlow<FloatArray> = _sensorData.asStateFlow()

    init {
        // 1. & 2. เมื่อได้รับค่าใหม่ นำค่าส่งเข้า StateFlow เพื่อให้อัปเดต UI ทันที
        sensorTracker.startListening { newValues ->
            _sensorData.value = newValues
        }
    }

    override fun onCleared() {
        super.onCleared()
        // หยุดการทำงานของ Sensor เมื่อ ViewModel ถูกทำลาย
        sensorTracker.stopListening()
    }
}
