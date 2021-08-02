package com.example.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.appwidget.AppWidgetManager
import android.content.pm.PackageManager
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import com.example.myapplication.databinding.ActivityMainBinding
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    val requestContacts = 1
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.extras != null) {
            val prefs = getSharedPreferences("widget_settings", MODE_PRIVATE)
            Log.e("DEBUG", "other count: ${prefs.getInt("count", 55)}")
            Toast.makeText(baseContext, "AVATARS COUNT: ${intent.extras?.getInt("count")}", Toast.LENGTH_SHORT).show()
        }


        binding.button.setOnClickListener {
            if (checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                printContacts()
            } else {
                requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), requestContacts)
            }
        }

    }

    @SuppressLint("Range")
    private fun printContacts() {
        val contentResolver = contentResolver
        val cur = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        if (cur?.count ?: 0 > 0) {
            while (cur != null && cur.moveToNext()) {

                val id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID))
                val name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))

                if(cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    val phoneCur: Cursor? = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id),
                        null
                    )

                    while(phoneCur?.moveToNext() == true) {
                        val phoneNumber = phoneCur.getString(phoneCur.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER
                        ))

                        Log.d("DEBUG", "\n\nName: $name\nPhone: $phoneNumber\n-=-\n\n")
                    }

                    phoneCur?.close()
                }
            }
        }

        cur?.close()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            printContacts()
        } else {
            Toast.makeText(baseContext, "I need this permission dude.", Toast.LENGTH_SHORT).show()
        }
    }
}