package com.example.a523lablearnandroid

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

/**
 * ตัวอย่างการเขียน Unit Test สำหรับ SensorTracker
 * ใช้ MockK ในการจำลอง (Mock) Android components เช่น Context และ SensorManager
 */
class SensorTrackerTest {

    // ตัวแปร Mock ที่ถูกจำลองขึ้นมาครอบคลุมการทำงานแทนของจริง
    private lateinit var mockContext: Context
    private lateinit var mockSensorManager: SensorManager
    private lateinit var mockSensor: Sensor
    private lateinit var sensorTracker: SensorTracker

    @Before
    fun setup() {
        // สร้าง mock object
        mockContext = mockk(relaxed = true)
        mockSensorManager = mockk(relaxed = true)
        mockSensor = mockk(relaxed = true)

        // กำหนดพฤติกรรม (Behavior) ให้กับ mock object
        every { mockContext.getSystemService(Context.SENSOR_SERVICE) } returns mockSensorManager
        every { mockSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) } returns mockSensor

        // สร้าง Instance ที่ต้องการทดสอบโดยเปรียบประหนึ่งว่า Context ถูกส่งเข้ามาจากในแอปจริงๆ
        sensorTracker = SensorTracker(mockContext)
    }

    @Test
    fun `test startListening registers sensor listener`() {
        // ทำการเรียกฟังก์ชัน
        sensorTracker.startListening { }

        // ตรวจสอบ (Verify) ว่ามีการนำ Context ตัวจำลองไป Register ค่าจริงหรือไม่
        verify {
            mockSensorManager.registerListener(
                any<SensorEventListener>(),
                mockSensor,
                SensorManager.SENSOR_DELAY_UI
            )
        }
    }

    @Test
    fun `test stopListening unregisters sensor listener`() {
        // เรียก start ให้มี listener ถูกเก็บไว้ในคลาส
        sensorTracker.startListening { }
        
        // ทดสอบการเรียก stopListening
        sensorTracker.stopListening()

        // ตรวจสอบว่า SensorManager ทำการ unregisterListener ออกจริง
        verify {
            mockSensorManager.unregisterListener(any<SensorEventListener>())
        }
    }
}
