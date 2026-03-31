package com.example.a523lablearnandroid

import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

/**
 * ตัวอย่างการเขียน Unit Test สำหรับ SensorViewModel
 * ทำการจำลอง Application Context ให้สามารถรันคู่กับ AndroidViewModel ได้
 */
class SensorViewModelTest {

    private lateinit var mockApplication: Application
    private lateinit var mockSensorManager: SensorManager
    private lateinit var mockSensor: Sensor
    private lateinit var viewModel: SensorViewModel

    @Before
    fun setup() {
        // เตรียม Mocked Objects
        mockApplication = mockk(relaxed = true)
        mockSensorManager = mockk(relaxed = true)
        mockSensor = mockk(relaxed = true)

        // กำหนดเงื่อนไขของการจำลอง Context เนื่องจาก SensorViewModel จะเรียกใช้งาน SensorTracker ทันทีที่ถูกสร้าง
        every { mockApplication.getSystemService(Context.SENSOR_SERVICE) } returns mockSensorManager
        every { mockSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) } returns mockSensor

        // สร้าง ViewModel
        viewModel = SensorViewModel(mockApplication)
    }

    @Test
    fun `test initial state of sensorData is array of 3 zeros`() {
        // ตรวจสอบค่าเริ่มต้นของ StateFlow ว่าเป็น 0f, 0f, 0f ตามที่กำหนดไว้ใน ViewModel หรือไม่
        val currentData = viewModel.sensorData.value
        
        assertNotNull(currentData)
        assertArrayEquals(floatArrayOf(0f, 0f, 0f), currentData, 0.001f)
    }
}
