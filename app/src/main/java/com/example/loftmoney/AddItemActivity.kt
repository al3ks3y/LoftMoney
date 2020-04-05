package com.example.loftmoney

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText

class AddItemActivity : AppCompatActivity() {

    private var mNameEditText: EditText? = null
    private var mPriceEditText: EditText? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        mNameEditText = findViewById(R.id.name_edittext)
        mPriceEditText = findViewById(R.id.price_edittext)

        val addButton = findViewById<Button>(R.id.add_button)
        addButton.setOnClickListener(View.OnClickListener {
            val name = mNameEditText!!.text.toString()
            val price = mPriceEditText!!.text.toString()

            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(price)) {
                setResult(
                    Activity.RESULT_OK,
                    Intent().putExtra("name", name).putExtra("price", price)
                )
                finish()
            }
        })
    }
}
