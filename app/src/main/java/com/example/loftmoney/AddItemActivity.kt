package com.example.loftmoney

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class AddItemActivity : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var addButton: Button
    private var name:String?=null
    private var price:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)
        nameEditText = findViewById(R.id.name_edittext)
        nameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun afterTextChanged(editable: Editable) {
                name = editable.toString()
                checkEditTextHasText()
            }
        })
        priceEditText = findViewById(R.id.price_edittext)
        priceEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun afterTextChanged(editable: Editable) {
                price = editable.toString()
                checkEditTextHasText()
            }
        })
        addButton = findViewById<Button>(R.id.add_button)
        addButton.setOnClickListener {
            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(price)) {
                setResult(
                    Activity.RESULT_OK,
                    Intent().putExtra("name", name).putExtra("price", price)
                )
                finish()
            }
        }
    }

    fun checkEditTextHasText() {
        addButton.setEnabled(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(price))
    }
}

