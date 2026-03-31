package com.example.a523lablearnandroid.utils

import android.content.Context
import android.content.SharedPreferences
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * ตัวอย่างการเขียน Unit Test สำหรับทดสอบการทำงานกับ SharedPreferences
 * ใช้ MockK ในการจำลอง SharedPreferences และ Editor
 */
class SharedPreferencesUtilTest {

    private lateinit var mockContext: Context
    private lateinit var mockPrefs: SharedPreferences
    private lateinit var mockEditor: SharedPreferences.Editor

    @Before
    fun setup() {
        // รีเซ็ทค่าตัวแปร sharedPreferences ใน Singleton object ด้วย Reflection ก่อนแต่ละ Test
        val field = SharedPreferencesUtil::class.java.getDeclaredField("sharedPreferences")
        field.isAccessible = true
        field.set(SharedPreferencesUtil, null)

        mockContext = mockk(relaxed = true)
        mockPrefs = mockk(relaxed = true)
        mockEditor = mockk(relaxed = true)

        // จำลองการเรียก Context.getSharedPreferences ให้คืนค่า mockPrefs กลับมา
        every { mockContext.getSharedPreferences(any(), any()) } returns mockPrefs
        
        // จำลองให้ prefs.edit() คืนค่าตัว editor (ที่ถูก mock ไว้)
        every { mockPrefs.edit() } returns mockEditor
        
        // จำลองให้ฟังก์ชันต่างๆ ของ editor เมื่อถูกเรียกแล้วต้อง return ตัวมันเอง (Chained method)
        every { mockEditor.putString(any(), any()) } returns mockEditor
        every { mockEditor.apply() } returns Unit

        // เรียก init เพื่อเตรียม SharedPreferencesUtil ของเราให้พร้อมใช้งานร่วมกับตัวที่ถูกจำลอง
        SharedPreferencesUtil.init(mockContext)
    }

    @Test
    fun `test saveString stores value in preferences`() {
        SharedPreferencesUtil.saveString("test_key", "test_value")

        // ตรวจสอบ (Verify) ว่ามีการเรียกใช้ .putString() และ .apply() สำหรับพฤติกรรมนี้จริง
        verify {
            mockEditor.putString("test_key", "test_value")
            mockEditor.apply()
        }
    }

    @Test
    fun `test getString retrieves value from preferences`() {
        // จำกัดสภาวะแวดล้อม: ถ้ามีการขออ่านค่า "test_key" ให้จำลองสมมุติคืนค่า "saved_value" ไป
        every { mockPrefs.getString("test_key", any()) } returns "saved_value"
        
        val result = SharedPreferencesUtil.getString("test_key")
        
        // ตรวจสอบผลลัพธ์
        assertEquals("saved_value", result)
    }
}
