package ru.mrfix1033.contentprovidercontent.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.telephony.SmsManager
import android.widget.Toast

class TelephonyUtils(val context: Context) {

    fun startCall(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$phoneNumber")
        context.startActivity(intent)
    }

    fun sendSMS(phoneNumber: String, message: String) {
        try {
            val smsManager = context.getSystemService(SmsManager::class.java)
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            Toast.makeText(context, "Сообщение отправлено", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Возникла ошибка: ${e.message.toString()}", Toast.LENGTH_LONG)
                .show()
        }
    }
}