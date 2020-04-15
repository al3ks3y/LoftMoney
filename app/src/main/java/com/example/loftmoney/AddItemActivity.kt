package com.example.loftmoney

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class AddItemActivity : AppCompatActivity() {
    private lateinit var mNameEditText: EditText
    private lateinit var mPriceEditText: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)
        mNameEditText = findViewById(R.id.name_edittext)
        mPriceEditText = findViewById(R.id.price_edittext)
        val addButton = findViewById<Button>(R.id.add_button)
        addButton.setOnClickListener {
            val name = mNameEditText.getText().toString()
            val price = mPriceEditText.getText().toString()
            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(price)) {
                setResult(
                    Activity.RESULT_OK,
                    Intent().putExtra("name", name).putExtra("price", price)
                )
                finish()
            }
        }
    }
}
