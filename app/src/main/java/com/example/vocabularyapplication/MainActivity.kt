package com.example.vocabularyapplication

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.vocabularyapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var bindingMain: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bindingMain = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingMain.root)

        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val savedUserNamed = sharedPreferences.getString("USER_NAME", null)
        if (savedUserNamed != null) {
            // direct to main page
            navigateToDashboard(savedUserNamed)
        } else {
            showOnboard()
        }
    }

    private fun showOnboard() {
        bindingMain.etNameOnboarding.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                bindingMain.tvTitle.text = getString(R.string.txt_title_default)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                bindingMain.tvTitle.text = getString(R.string.txt_title_replaced)
            }

            override fun afterTextChanged(s: Editable?) {
                bindingMain.tvTitle.text = getString(R.string.txt_title_replaced)
                bindingMain.btnStart.isVisible = !s.isNullOrEmpty()
            }
        })
        bindingMain.btnStart.setOnClickListener {
            val userName = bindingMain.etNameOnboarding.text.toString()
            saveName(userName)
            navigateToDashboard(userName)
        }
    }

    private fun saveName(userName: String) {
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("USER_NAME", userName)
        editor.apply()
    }

    private fun navigateToDashboard(userName: String) {
        val intent = Intent(this, DashboardActivity::class.java)
        intent.putExtra("USER_NAME", userName)
        startActivity(intent)
    }
}