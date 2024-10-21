package com.example.vocabularyapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.vocabularyapplication.databinding.ActivityAddBinding
import com.example.vocabularyapplication.db.SqlDbHandler
import com.example.vocabularyapplication.model.WordCategory

class AddActivity : AppCompatActivity() {
    private lateinit var bindingAddActivity: ActivityAddBinding
    private val sqlDbHandler: SqlDbHandler = SqlDbHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bindingAddActivity = ActivityAddBinding.inflate(layoutInflater)
        setContentView(bindingAddActivity.root)
        setSpinner()

        bindingAddActivity.btnSave.setOnClickListener {
            if (bindingAddActivity.etName.text.isNullOrEmpty() ||
                bindingAddActivity.etMeaning.text.isNullOrEmpty() ||
                bindingAddActivity.spinnerCategory.selectedItem.toString().isEmpty()
            ) return@setOnClickListener

            sqlDbHandler.addVocab(
                bindingAddActivity.etName.text.toString(),
                bindingAddActivity.etMeaning.text.toString(),
                bindingAddActivity.spinnerCategory.selectedItem.toString()
            )
            setResult(123, Intent())
            finish()
        }

        bindingAddActivity.btnDiscard.setOnClickListener{
            onBackPressed()
        }
    }

    private fun setSpinner() {
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, getCategoryList()
        )
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        bindingAddActivity.spinnerCategory.adapter = adapter
    }

    private fun getCategoryList(): List<String> {
        return WordCategory.values().map {
            if (it == WordCategory.ALL_CATEGORY) {
                ""
            } else {
                it.title
            }
        }
    }
}